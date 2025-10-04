package com.ganoninc.viteurlshortener.authservice.service;


import com.ganoninc.viteurlshortener.authservice.dto.TokenPairDto;

public interface RefreshTokenService {
    String createRefreshToken(String userEmail);

    TokenPairDto validateAndRotateTokens(String refreshToken);
}
