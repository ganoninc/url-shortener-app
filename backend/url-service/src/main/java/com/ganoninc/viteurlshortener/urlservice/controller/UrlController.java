package com.ganoninc.viteurlshortener.urlservice.controller;

import com.ganoninc.viteurlshortener.urlservice.common.UserContext;
import com.ganoninc.viteurlshortener.urlservice.dto.ShortenURLRequestDTO;
import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;
import com.ganoninc.viteurlshortener.urlservice.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<Map<String,String>> shortenUrl(@RequestBody @Valid ShortenURLRequestDTO requestDTO) {
        String url = requestDTO.getOriginalUrl();

        String userEmail = UserContext.getUserEmail();
        if(userEmail == null) {
            return ResponseEntity.badRequest().body(new HashMap<>());
        }

        UrlMapping urlMapping = urlService.createUrlMapping(url, userEmail);
        return ResponseEntity.ok(Map.of("shortId", urlMapping.getShortId()));
    }

    @GetMapping("/my-urls")
    public List<UrlMapping> getMyUrls() {
        String userEmail = UserContext.getUserEmail();
        return urlService.getUserUrls(userEmail);
    }
}
