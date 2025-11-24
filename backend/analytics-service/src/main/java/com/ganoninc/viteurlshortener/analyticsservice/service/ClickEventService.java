package com.ganoninc.viteurlshortener.analyticsservice.service;


import java.time.Instant;

public interface ClickEventService {
  void createClickEvent(String shortId, Instant timestamp, String ip, String userAgent);
}
