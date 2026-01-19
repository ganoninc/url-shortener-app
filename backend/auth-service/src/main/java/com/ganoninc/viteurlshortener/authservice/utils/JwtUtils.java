package com.ganoninc.viteurlshortener.authservice.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expirationMs}")
  private Long expiration;

  public String generateToken(String email) {
    String token =
        Jwts.builder()
            .setSubject(email)
            .setIssuer("auth-service")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(
                Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)),
                SignatureAlgorithm.HS256)
            .compact();

    Span.current()
        .addEvent(
            "jwt_token_generated",
            Attributes.of(AttributeKey.longKey("auth.jwt.expiration"), expiration));

    return token;
  }
}
