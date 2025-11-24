package com.ganoninc.viteurlshortener.analyticsservice.service.impl;

import com.ganoninc.viteurlshortener.analyticsservice.service.GeoIpCacheService;
import com.ganoninc.viteurlshortener.analyticsservice.service.GeoIpService;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class GeoIpServiceImpl implements GeoIpService {
  private final GeoIpCacheService geoIpCacheService;

  public GeoIpServiceImpl(GeoIpCacheService geoIpCacheService) {
    this.geoIpCacheService = geoIpCacheService;
  }

  @Async
  @Override
  public CompletableFuture<String> getCountryOfIp(String ip) {
    return CompletableFuture.supplyAsync(() -> geoIpCacheService.fetchCountry(ip))
        .exceptionally(e -> "-E");
  }
}
