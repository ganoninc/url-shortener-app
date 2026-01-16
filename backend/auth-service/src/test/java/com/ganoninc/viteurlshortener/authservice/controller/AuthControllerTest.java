package com.ganoninc.viteurlshortener.authservice.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ganoninc.viteurlshortener.authservice.config.AppProperties;
import com.ganoninc.viteurlshortener.authservice.config.SecurityConfig;
import com.ganoninc.viteurlshortener.authservice.dto.TokenPairDto;
import com.ganoninc.viteurlshortener.authservice.exception.RefreshTokenInvalidException;
import com.ganoninc.viteurlshortener.authservice.service.AuthService;
import com.ganoninc.viteurlshortener.authservice.service.RefreshTokenService;
import com.ganoninc.viteurlshortener.authservice.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import java.util.List;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {

  @Autowired private MockMvc mvc;
  @MockitoBean private AuthService authService;
  @MockitoBean private AppProperties appProperties;

  @Test
  public void itShouldReturnOauthCallbackViewWithModelAttributesAndARefreshTokenCookie()
      throws Exception {
    String email = "test@gmail.com";
    String token = "jwt-token";
    String refreshToken = "refresh.token";
    String frontendOrigin = "http://localhost:8080";

    when(authService.generateAccessToken(email)).thenReturn(token);
    when(appProperties.getFrontendOrigin()).thenReturn(frontendOrigin);
    when(authService.createRefreshToken(email)).thenReturn(refreshToken);

    OAuth2User oAuth2User =
        new DefaultOAuth2User(
            List.of(new SimpleGrantedAuthority("ROLE_USER")), Map.of("email", email), "email");

    mvc.perform(
            MockMvcRequestBuilders.get("/oauth-callback")
                .with(oauth2Login().oauth2User(oAuth2User)))
        .andExpect(status().isOk())
        .andExpect(view().name("oauth-callback"))
        .andExpect(model().attribute("token", token))
        .andExpect(model().attribute("email", email))
        .andExpect(model().attribute("frontendOrigin", frontendOrigin))
        .andExpect(cookie().exists("refresh-token"))
        .andExpect(cookie().value("refresh-token", refreshToken));

    verify(authService).generateAccessToken(email);
    verify(authService).createRefreshToken(email);
    verify(appProperties).getFrontendOrigin();
  }

  @Test
  public void itShouldBlockAccessIfUserIsNotLoggedIn() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/oauth-callback"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("http://localhost/oauth2/authorization/*"));
  }

  @Test
  public void itShouldReturnANewAccessTokenAndANewRefreshTokenCookie() throws Exception {
    String oldRefreshToken = "old-refresh-token";
    String newRefreshToken = "new-refresh-token";
    String newAccessToken = "new-access-token";

    when(authService.validateAndRotateTokens(oldRefreshToken))
        .thenReturn(new TokenPairDto(newAccessToken, newRefreshToken));

    mvc.perform(
            MockMvcRequestBuilders.get("/refresh-access-token")
                .cookie(new Cookie("refresh-token", oldRefreshToken)))
        .andExpect(status().isOk())
        .andExpect(content().string(newAccessToken))
        .andExpect(cookie().exists("refresh-token"))
        .andExpect(cookie().value("refresh-token", newRefreshToken))
        .andExpect(
            header()
                .stringValues("Set-Cookie", Matchers.hasItem(Matchers.containsString("HttpOnly"))))
        .andExpect(header().string("Set-Cookie", Matchers.containsString("SameSite=Strict")));

    verify(authService, times(1)).validateAndRotateTokens(oldRefreshToken);
  }

  @Test
  public void itShouldReturnBadRequestOnException() throws Exception {
    String oldRefreshToken = "old-refresh-token";

    when(authService.validateAndRotateTokens(oldRefreshToken))
        .thenThrow(new RefreshTokenInvalidException("Invalid refresh token"));

    mvc.perform(
            MockMvcRequestBuilders.get("/refresh-access-token")
                .cookie(new Cookie("refresh-token", oldRefreshToken)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Invalid refresh token"));

    verify(authService, times(1)).validateAndRotateTokens(oldRefreshToken);
  }
}
