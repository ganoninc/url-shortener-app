package com.ganoninc.viteurlshortener.analyticsservice.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ganoninc.viteurlshortener.analyticsservice.service.GeoIpCacheService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeoIpServiceImplTest {

  @Mock GeoIpCacheService geoIpCacheService;
  @InjectMocks GeoIpServiceImpl geoIpService;

  @Test
  void itShouldReturnCountryFromCacheService() throws ExecutionException, InterruptedException {
    String ip = "192.168.1.1";

    when(geoIpCacheService.fetchCountry(ip)).thenReturn("FR");

    CompletableFuture<String> future = geoIpService.getCountryOfIp(ip);
    String result = future.get();

    assertEquals("FR", result);
    verify(geoIpCacheService).fetchCountry(ip);
  }
}
