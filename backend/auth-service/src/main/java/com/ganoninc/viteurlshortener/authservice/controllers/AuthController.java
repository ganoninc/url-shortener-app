package com.ganoninc.viteurlshortener.authservice.controllers;

import com.ganoninc.viteurlshortener.authservice.config.AppProperties;
import com.ganoninc.viteurlshortener.authservice.utils.JwtUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/identity")
public class AuthController {

    private final JwtUtils jwtUtils;
    private final AppProperties appProperties;

    public AuthController(JwtUtils jwtUtils, AppProperties appProperties) {
        this.jwtUtils = jwtUtils;
        this.appProperties = appProperties;
    }

    @GetMapping("/token")
    public String getToken(Model model, @AuthenticationPrincipal OAuth2User user) {
        String email = user.getAttribute("email");
        String token = jwtUtils.generateToken(email);

        model.addAttribute("token", token);
        model.addAttribute("email", email);
        model.addAttribute("frontendOrigin", appProperties.getFrontendOrigin());

        return "token";
    }
}
