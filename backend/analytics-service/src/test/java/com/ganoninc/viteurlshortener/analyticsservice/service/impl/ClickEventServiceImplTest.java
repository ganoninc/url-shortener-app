package com.ganoninc.viteurlshortener.analyticsservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.ganoninc.viteurlshortener.analyticsservice.model.ClickEvent;
import com.ganoninc.viteurlshortener.analyticsservice.repository.ClickRepository;
import com.ganoninc.viteurlshortener.analyticsservice.service.GeoIpService;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClickEventServiceImplTest {
  @InjectMocks ClickEventServiceImpl clickEventServiceImpl;
  @Mock ClickRepository clickRepository;
  @Mock GeoIpService geoIpService;

  @Test
  public void itShouldSaveClickEventAndAddCountry() {
    String shortId = "abcd123";
    String ipAddress = "192.168.1.1";
    String userAgent = "Mozilla";
    String countryIsoCode = "US";

    ArgumentCaptor<ClickEvent> captor = ArgumentCaptor.forClass(ClickEvent.class);

    when(clickRepository.save(any(ClickEvent.class))).thenAnswer(i -> i.getArgument(0));
    when(geoIpService.getCountryOfIp(ipAddress))
        .thenReturn(CompletableFuture.completedFuture(countryIsoCode));

    clickEventServiceImpl.createClickEvent(shortId, Instant.now(), ipAddress, userAgent);

    verify(clickRepository, times(2)).save(captor.capture());
    verify(geoIpService).getCountryOfIp(ipAddress);

    ClickEvent savedClickEvent = captor.getValue();
    assertEquals(shortId, savedClickEvent.getShortId());
    assertEquals(countryIsoCode, savedClickEvent.getCountryCode());
  }
}
