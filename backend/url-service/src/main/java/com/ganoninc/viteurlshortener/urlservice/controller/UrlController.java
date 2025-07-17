package com.ganoninc.viteurlshortener.urlservice.controller;

import com.ganoninc.viteurlshortener.urlservice.dto.ShortenURLRequestDTO;
import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;
import com.ganoninc.viteurlshortener.urlservice.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody @Valid ShortenURLRequestDTO requestDTO, @AuthenticationPrincipal Jwt jwt) {
        String userEmail = jwt.getSubject();
        String url = requestDTO.getOriginalUrl();

        UrlMapping urlMapping = urlService.createUrlMapping(url, userEmail);
        return ResponseEntity.ok(Map.of("shortId", urlMapping.getShortId()));
    }

    @GetMapping("/my-urls")
    public List<UrlMapping> getMyUrls(@AuthenticationPrincipal Jwt jwt){
        return urlService.getUserUrls(jwt.getSubject());
    }
}
