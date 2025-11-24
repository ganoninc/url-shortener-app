package com.ganoninc.viteurlshortener.analyticsservice.service;

public interface GeoIpCacheService {
  String fetchCountry(String ip);
}
