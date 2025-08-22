package com.ganoninc.viteurlshortener.analyticsservice.controller;

import com.ganoninc.viteurlshortener.analyticsservice.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/{shortId}")
    public ResponseEntity<?> getStats(@PathVariable String shortId) {
        Map<String, Object> result = new HashMap<>();
        result.put("clickCount", analyticsService.getClickCount(shortId));
        result.put("events", analyticsService.getAllEvents(shortId));

        return ResponseEntity.ok(result);
    }
}
