package com.ganoninc.viteurlshortener.analyticsservice.service;

import com.ganoninc.viteurlshortener.analyticsservice.model.ClickEvent;

import java.time.Instant;

public record ClickEventView(
    Long id, String ShortId, Instant timestamp, String ip, String userAgent, String countryCode) {

  public static ClickEventView from(ClickEvent clickEvent) {
    return new ClickEventView(
        clickEvent.getId(),
        clickEvent.getShortId(),
        clickEvent.getTimestamp(),
        clickEvent.getIp(),
        clickEvent.getUserAgent(),
        clickEvent.getCountryCode());
  }
}
