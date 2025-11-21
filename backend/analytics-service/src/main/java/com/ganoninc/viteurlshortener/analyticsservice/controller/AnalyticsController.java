package com.ganoninc.viteurlshortener.analyticsservice.controller;

import com.ganoninc.viteurlshortener.analyticsservice.dto.ShortUrlClickEventsDTO;
import com.ganoninc.viteurlshortener.analyticsservice.service.AnalyticsService;
import com.ganoninc.viteurlshortener.analyticsservice.service.PaginatedClickEvents;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyticsController {
  private final AnalyticsService analyticsService;

  public AnalyticsController(AnalyticsService analyticsService) {
    this.analyticsService = analyticsService;
  }

  @GetMapping("/short-url/{shortId}")
  public ResponseEntity<ShortUrlClickEventsDTO> getClickEvents(
      @PathVariable String shortId,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "50") int size) {
    try {
      Long decodedCursor = null;

      if (cursor != null) {
        decodedCursor =
            Long.parseLong(
                new String(Base64.getUrlDecoder().decode(cursor), StandardCharsets.UTF_8));
      }

      PaginatedClickEvents events = analyticsService.getClickEvents(shortId, decodedCursor, size);

      return ResponseEntity.ok(ShortUrlClickEventsDTO.from(events));
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }
}
