package com.ganoninc.viteurlshortener.authservice.controller;

import com.ganoninc.viteurlshortener.authservice.config.AppProperties;
import com.ganoninc.viteurlshortener.authservice.dto.TokenPairDto;
import com.ganoninc.viteurlshortener.authservice.service.AuthService;
import com.ganoninc.viteurlshortener.authservice.service.RefreshTokenService;
import com.ganoninc.viteurlshortener.authservice.utils.JwtUtils;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

  private final AuthService authService;
  private final AppProperties appProperties;
  private final String refreshTokenCookieName = "refresh-token";

  @Value("${jwt.refreshTokenExpirationMs}")
  private Integer refreshTokenExpirationInMs;

  public AuthController(AuthService authService, AppProperties appProperties) {
    this.authService = authService;
    this.appProperties = appProperties;
  }

  @GetMapping("/oauth-callback")
  public String handleOAuthCallback(
      Model model, @AuthenticationPrincipal OAuth2User user, HttpServletResponse response) {
    String email = user.getAttribute("email");

    String accessToken = authService.generateAccessToken(email);
    String refreshToken = authService.createRefreshToken(email);

    ResponseCookie refreshTokenCookie =
        ResponseCookie.from(refreshTokenCookieName, refreshToken)
            .httpOnly(true)
            .sameSite("Strict")
            .secure(true)
            .path("/")
            .maxAge(refreshTokenExpirationInMs)
            .build();
    response.addHeader("Set-Cookie", refreshTokenCookie.toString());

    model.addAttribute("token", accessToken);
    model.addAttribute("email", email);
    model.addAttribute("frontendOrigin", appProperties.getFrontendOrigin());

    return "oauth-callback";
  }

  @GetMapping("/refresh-access-token")
  public ResponseEntity<Object> handleRefreshAccessToken(
      @CookieValue(refreshTokenCookieName) String refreshToken, HttpServletResponse response) {
    try {
      TokenPairDto tokenPair = authService.validateAndRotateTokens(refreshToken);

      ResponseCookie refreshTokenCookie =
          ResponseCookie.from(refreshTokenCookieName, tokenPair.refreshToken())
              .httpOnly(true)
              .sameSite("Strict")
              .secure(true)
              .path("/")
              .maxAge(refreshTokenExpirationInMs)
              .build();

      response.addHeader("Set-Cookie", refreshTokenCookie.toString());

      return ResponseEntity.ok(tokenPair.jwtAccessToken());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
