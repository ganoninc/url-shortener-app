package com.ganoninc.viteurlshortener.authservice.service.impl;

import com.ganoninc.viteurlshortener.authservice.dto.TokenPairDto;
import com.ganoninc.viteurlshortener.authservice.service.AuthService;
import com.ganoninc.viteurlshortener.authservice.service.RefreshTokenService;
import com.ganoninc.viteurlshortener.authservice.utils.JwtUtils;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  private final JwtUtils jwtUtils;
  private final RefreshTokenService refreshTokenService;

  public AuthServiceImpl(JwtUtils jwtUtils, RefreshTokenService refreshTokenService) {
    this.jwtUtils = jwtUtils;
    this.refreshTokenService = refreshTokenService;
  }

  @WithSpan("auth.generate_access_token")
  public String generateAccessToken(String email) {
    return jwtUtils.generateToken(email);
  }

  @WithSpan("auth.create_refresh_token")
  public String createRefreshToken(String email) {
    return refreshTokenService.createRefreshToken(email);
  }

  @WithSpan("auth.validate_and_rotate_tokens")
  public TokenPairDto validateAndRotateTokens(String refreshToken) {
    Span.current().setAttribute("auth.flow", "refresh");
    return refreshTokenService.validateAndRotateTokens(refreshToken);
  }
}
