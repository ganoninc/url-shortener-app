package com.ganoninc.viteurlshortener.authservice.service.impl;

import com.ganoninc.viteurlshortener.authservice.dto.TokenPairDto;
import com.ganoninc.viteurlshortener.authservice.exception.RefreshTokenInvalidException;
import com.ganoninc.viteurlshortener.authservice.exception.RefreshTokenNotFoundException;
import com.ganoninc.viteurlshortener.authservice.model.RefreshTokenMapping;
import com.ganoninc.viteurlshortener.authservice.repository.RefreshTokenRepository;
import com.ganoninc.viteurlshortener.authservice.service.RefreshTokenService;
import com.ganoninc.viteurlshortener.authservice.utils.JwtUtils;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

  private final JwtUtils jwtUtils;
  private final PasswordEncoder encoder;
  private final RefreshTokenRepository refreshTokenRepository;
  private final Logger logger = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);

  @Value("${jwt.refreshTokenExpirationMs}")
  private Long refreshTokenExpirationMs;

  public RefreshTokenServiceImpl(
      RefreshTokenRepository refreshTokenRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.encoder = encoder;
    this.jwtUtils = jwtUtils;
  }

  @Transactional
  public String createRefreshToken(String userEmail) {
    UUID tokenId = UUID.randomUUID();
    String token = generateRawToken();
    String tokenHash = encoder.encode(token);

    RefreshTokenMapping refreshTokenMapping =
        RefreshTokenMapping.builder()
            .id(tokenId)
            .createdAt(Instant.now())
            .expiresAt(Instant.now().plusMillis(refreshTokenExpirationMs))
            .userEmail(userEmail)
            .tokenHash(tokenHash)
            .build();

    refreshTokenRepository.save(refreshTokenMapping);

    Span.current()
        .addEvent(
            "refresh_token_created",
            Attributes.of(AttributeKey.stringKey("auth.refresh_token_id"), tokenId.toString()));

    return tokenId + "." + token;
  }

  @WithSpan("auth.refresh_token.validate_and_rotate")
  @Transactional
  public TokenPairDto validateAndRotateTokens(String rawRefreshToken) {
    String[] parts = rawRefreshToken.split("\\.");
    if (parts.length != 2) {
      String maskedToken =
          rawRefreshToken.length() > 8
              ? rawRefreshToken.substring(0, 4)
                  + "..."
                  + rawRefreshToken.substring(rawRefreshToken.length() - 4)
              : "****";
      logger.error("Invalid refresh token. Validation failed. Token: {}", maskedToken);
      throw new RefreshTokenInvalidException("Invalid refresh token");
    }

    UUID tokenId = UUID.fromString(parts[0]);
    Span.current().setAttribute("auth.refresh_token_id", tokenId.toString());
    String token = parts[1];

    RefreshTokenMapping currentRefreshTokenMapping =
        refreshTokenRepository
            .findByIdForUpdate(tokenId)
            .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found"));

    validateCurrentRefreshToken(currentRefreshTokenMapping, token);

    return revokeCurrentTokenAndCreateNewTokens(currentRefreshTokenMapping);
  }

  @Transactional
  protected TokenPairDto revokeCurrentTokenAndCreateNewTokens(
      RefreshTokenMapping currentRefreshTokenMapping) {
    String newRefreshToken = createRefreshToken(currentRefreshTokenMapping.getUserEmail());
    String newJwtAccessToken = jwtUtils.generateToken(currentRefreshTokenMapping.getUserEmail());

    currentRefreshTokenMapping.setRevokedAt(Instant.now());
    currentRefreshTokenMapping.setReplacement(
        refreshTokenRepository
            .findById(UUID.fromString(newRefreshToken.split("\\.")[0]))
            .orElseThrow(() -> new RefreshTokenNotFoundException("New refresh token not found")));
    refreshTokenRepository.save(currentRefreshTokenMapping);

    Span.current().addEvent("refresh_token_rotated");

    return new TokenPairDto(newJwtAccessToken, newRefreshToken);
  }

  private void validateCurrentRefreshToken(
      RefreshTokenMapping currentRefreshTokenMapping, String token) {

    if (currentRefreshTokenMapping.getExpiresAt().isBefore(Instant.now())) {
      throw new RefreshTokenInvalidException("Expired refresh token");
    }

    if (!encoder.matches(token, currentRefreshTokenMapping.getTokenHash())) {
      throw new RefreshTokenInvalidException("Invalid refresh token");
    }

    if (currentRefreshTokenMapping.getRevokedAt() != null) {
      revokeChain(currentRefreshTokenMapping);
      throw new RefreshTokenInvalidException("Revoked refresh token");
    }
  }

  private String generateRawToken() {
    byte[] randomBytes = new byte[53];
    new SecureRandom().nextBytes(randomBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
  }

  private void revokeChain(RefreshTokenMapping mapping) {
    while (mapping != null) {
      mapping.setRevokedAt(Instant.now());
      refreshTokenRepository.save(mapping);
      mapping = mapping.getReplacement();
    }
    Span.current().addEvent("refresh_token_chain_revoked");
  }
}
