package com.ganoninc.viteurlshortener.authservice.service;

import com.ganoninc.viteurlshortener.authservice.dto.TokenPairDto;

public interface AuthService {
  String generateAccessToken(String email);

  String createRefreshToken(String email);

  TokenPairDto validateAndRotateTokens(String refreshToken);
}
