package com.ganoninc.viteurlshortener.analyticsservice.service.impl;

import com.ganoninc.viteurlshortener.analyticsservice.service.GeoIpCacheService;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.time.Duration;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GeoIpCacheServiceImpl implements GeoIpCacheService {
  private final WebClient webClient;

  public GeoIpCacheServiceImpl(WebClient webClient) {
    this.webClient = webClient;
  }

  @WithSpan("analytics.get_country_of_ip")
  @Cacheable(value = "geoIpCache", key = "#ip")
  public String fetchCountry(@SpanAttribute("click_event.ip") String ip) {
    return webClient
        .get()
        .uri("https://ipapi.co/" + ip + "/country")
        .retrieve()
        .bodyToMono(String.class)
        .block(Duration.ofSeconds(5));
  }
}
