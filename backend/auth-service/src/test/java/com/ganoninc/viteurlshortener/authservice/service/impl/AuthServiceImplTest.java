package com.ganoninc.viteurlshortener.authservice.service.impl;

import com.ganoninc.viteurlshortener.authservice.dto.TokenPairDto;
import com.ganoninc.viteurlshortener.authservice.service.RefreshTokenService;
import com.ganoninc.viteurlshortener.authservice.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

  @Mock private JwtUtils jwtUtils;
  @Mock private RefreshTokenService refreshTokenService;

  @InjectMocks private AuthServiceImpl authServiceImpl;

  @Test
  public void itShouldGenerateAccessToken() {
    String email = "test@gmail.com";
    String token = "jwt-token";

    when(jwtUtils.generateToken(email)).thenReturn(token);

    String result = authServiceImpl.generateAccessToken(email);

    assertThat(result).isEqualTo(token);
    verify(jwtUtils, times(1)).generateToken(email);
  }

  @Test
  public void itShouldCreateRefreshToken() {
    String email = "test@gmail.com";
    String token = "refresh-token";

    when(refreshTokenService.createRefreshToken(email)).thenReturn(token);

    String result = authServiceImpl.createRefreshToken(email);

    assertThat(result).isEqualTo(token);
    verify(refreshTokenService, times(1)).createRefreshToken(email);
  }

  @Test
  public void itShouldValidateAndRotateTokens() {
    String currentRefreshToken = "current-refresh-token";
    TokenPairDto expectedTokenPair = new TokenPairDto("new-access-token", "new-refresh-token");

    when(refreshTokenService.validateAndRotateTokens(currentRefreshToken))
        .thenReturn(expectedTokenPair);

    TokenPairDto result = authServiceImpl.validateAndRotateTokens(currentRefreshToken);

    assertThat(result).isEqualTo(expectedTokenPair);
    verify(refreshTokenService, times(1)).validateAndRotateTokens(currentRefreshToken);
  }
}
