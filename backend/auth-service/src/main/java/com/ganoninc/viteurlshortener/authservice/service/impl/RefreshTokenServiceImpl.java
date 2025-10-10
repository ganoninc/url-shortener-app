package com.ganoninc.viteurlshortener.authservice.service.impl;

import com.ganoninc.viteurlshortener.authservice.dto.TokenPairDto;
import com.ganoninc.viteurlshortener.authservice.exception.RefreshTokenInvalidException;
import com.ganoninc.viteurlshortener.authservice.exception.RefreshTokenNotFoundException;
import com.ganoninc.viteurlshortener.authservice.model.RefreshTokenMapping;
import com.ganoninc.viteurlshortener.authservice.repository.RefreshTokenRepository;
import com.ganoninc.viteurlshortener.authservice.service.RefreshTokenService;
import com.ganoninc.viteurlshortener.authservice.utils.JwtUtils;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

  private final JwtUtils jwtUtils;
  private final PasswordEncoder encoder;
  private final RefreshTokenRepository refreshTokenRepository;

  @Value("${jwt.refreshExpirationMs}")
  private Long refreshTokenDurationMs;

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
            .expiresAt(Instant.now().plusMillis(refreshTokenDurationMs))
            .userEmail(userEmail)
            .tokenHash(tokenHash)
            .build();

    refreshTokenRepository.save(refreshTokenMapping);

    return tokenId + "." + token;
  }

  @Transactional
  public TokenPairDto validateAndRotateTokens(String rawRefreshToken) {
    String[] parts = rawRefreshToken.split("\\.");
    if (parts.length != 2) {
      throw new RefreshTokenInvalidException("Invalid refresh token");
    }

    UUID tokenId = UUID.fromString(parts[0]);
    String token = parts[1];

    RefreshTokenMapping currentRefreshTokenMapping =
        refreshTokenRepository
            .findByIdForUpdate(tokenId)
            .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found"));

    validateCurrentRefreshToken(currentRefreshTokenMapping, token);

    return rovokeCurrentTokenAndCreateNewTokens(currentRefreshTokenMapping);
  }

  @Transactional
  protected TokenPairDto rovokeCurrentTokenAndCreateNewTokens(
      RefreshTokenMapping currentRefreshTokenMapping) {
    String newRefreshToken = createRefreshToken(currentRefreshTokenMapping.getUserEmail());
    String newJwtAccessToken = jwtUtils.generateToken(currentRefreshTokenMapping.getUserEmail());

    currentRefreshTokenMapping.setRevokedAt(Instant.now());
    currentRefreshTokenMapping.setReplacement(
        refreshTokenRepository
            .findById(UUID.fromString(newRefreshToken.split("\\.")[0]))
            .orElseThrow(() -> new RefreshTokenNotFoundException("New refresh token not found")));
    refreshTokenRepository.save(currentRefreshTokenMapping);

    return new TokenPairDto(newJwtAccessToken, newRefreshToken);
  }

  private void validateCurrentRefreshToken(
      RefreshTokenMapping currentRefreshTokenMapping, String token) {
    if (currentRefreshTokenMapping.getExpiresAt().compareTo(Instant.now()) < 0) {
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

  public String generateRawToken() {
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
  }
}
