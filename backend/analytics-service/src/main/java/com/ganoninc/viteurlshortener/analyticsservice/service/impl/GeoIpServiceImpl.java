package com.ganoninc.viteurlshortener.analyticsservice.service.impl;

import com.ganoninc.viteurlshortener.analyticsservice.service.GeoIpCacheService;
import com.ganoninc.viteurlshortener.analyticsservice.service.GeoIpService;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class GeoIpServiceImpl implements GeoIpService {
  private final GeoIpCacheService geoIpCacheService;

  public GeoIpServiceImpl(GeoIpCacheService geoIpCacheService) {
    this.geoIpCacheService = geoIpCacheService;
  }

  @WithSpan("analytics.add_country_of_click_event")
  @Async
  @Override
  public CompletableFuture<String> getCountryOfIp(@SpanAttribute("click_event.ip") String ip) {
    Span span = Span.current();
    String country = geoIpCacheService.fetchCountry(ip);

    if (country != null && !country.equalsIgnoreCase("undefined")) {
      span.setAttribute("geoip.country", country);
      return CompletableFuture.completedFuture(country);
    }

    return CompletableFuture.completedFuture(null);
  }
}
