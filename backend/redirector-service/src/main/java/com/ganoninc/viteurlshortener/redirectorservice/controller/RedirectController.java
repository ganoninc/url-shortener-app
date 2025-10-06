package com.ganoninc.viteurlshortener.redirectorservice.controller;

import com.ganoninc.viteurlshortener.redirectorservice.service.RedirectorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@RestController
public class RedirectController {
  private final RedirectorService redirectorService;

  public RedirectController(RedirectorService redirectorService) {
    this.redirectorService = redirectorService;
  }

  @GetMapping("/{shortId}")
  public ResponseEntity<Object> redirect(@PathVariable String shortId, HttpServletRequest request) {
    Optional<String> targetUrl =
        redirectorService.resolveRedirect(
            shortId, request.getRemoteAddr(), request.getHeader("User-Agent"));

    return targetUrl
        .map(url -> ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build())
        .orElse(ResponseEntity.notFound().build());
  }
}
