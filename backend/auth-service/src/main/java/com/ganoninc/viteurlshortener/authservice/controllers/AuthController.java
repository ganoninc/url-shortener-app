package com.ganoninc.viteurlshortener.authservice.controllers;

import com.ganoninc.viteurlshortener.authservice.utils.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtils jwtUtils;

    public AuthController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/token")
    public ResponseEntity<?> getToken(@AuthenticationPrincipal OAuth2User user){
        String email = user.getAttribute("email");
        String token = jwtUtils.generateToken(email);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
