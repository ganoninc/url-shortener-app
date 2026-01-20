package com.ganoninc.viteurlshortener.analyticsservice.service.impl;

import static com.ganoninc.viteurlshortener.analyticsservice.tracing.TelemetryAttributes.CLICK_EVENT_ID;

import com.ganoninc.viteurlshortener.analyticsservice.model.ClickEvent;
import com.ganoninc.viteurlshortener.analyticsservice.repository.ClickRepository;
import com.ganoninc.viteurlshortener.analyticsservice.service.ClickEventService;
import com.ganoninc.viteurlshortener.analyticsservice.service.GeoIpService;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class ClickEventServiceImpl implements ClickEventService {
  private final ClickRepository clickRepository;
  private final GeoIpService geoIpService;

  public ClickEventServiceImpl(ClickRepository clickRepository, GeoIpService geoIpService) {
    this.clickRepository = clickRepository;
    this.geoIpService = geoIpService;
  }

  @Override
  public void createClickEvent(String shortId, Instant timestamp, String ip, String userAgent) {
    ClickEvent clickEvent = new ClickEvent();
    clickEvent.setShortId(shortId);
    clickEvent.setTimestamp(timestamp);
    clickEvent.setIp(ip);
    clickEvent.setUserAgent(userAgent);
    clickRepository.save(clickEvent);
    Span.current()
        .addEvent("click_event.created", Attributes.of(CLICK_EVENT_ID, clickEvent.getId()));

    geoIpService
        .getCountryOfIp(ip)
        .thenAccept(
            country -> {
              clickEvent.setCountryCode(country);
              clickRepository.save(clickEvent);
            });
  }
}
