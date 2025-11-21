package com.ganoninc.viteurlshortener.analyticsservice.service;

public interface AnalyticsService {
  long getClickCount(String shortId);

  PaginatedClickEvents getClickEvents(String shortId, Long cursor, int size);
}
