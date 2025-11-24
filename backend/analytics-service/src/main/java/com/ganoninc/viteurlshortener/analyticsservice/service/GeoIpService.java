package com.ganoninc.viteurlshortener.analyticsservice.service;

import java.util.concurrent.CompletableFuture;

public interface GeoIpService {
  CompletableFuture<String> getCountryOfIp(String ip);
}
