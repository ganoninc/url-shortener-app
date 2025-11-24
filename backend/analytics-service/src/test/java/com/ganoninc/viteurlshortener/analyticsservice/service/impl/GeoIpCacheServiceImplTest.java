package com.ganoninc.viteurlshortener.analyticsservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class GeoIpCacheServiceImplTest {
  @InjectMocks GeoIpCacheServiceImpl geoIpCacheService;

  @Mock WebClient webClient;

  @SuppressWarnings("rawtypes")
  @Mock
  WebClient.RequestHeadersUriSpec uriSpec;

  @SuppressWarnings("rawtypes")
  @Mock
  WebClient.RequestHeadersSpec headersSpec;

  @Mock WebClient.ResponseSpec responseSpec;

  @BeforeEach
  void setup() {
    geoIpCacheService = new GeoIpCacheServiceImpl(webClient);
  }

  @Test
  public void itShouldUseWebClientToGetCountryIsoCode() {
    String countryIsoCode = "US";
    String IPAddress = "1.2.3.4";

    when(webClient.get()).thenReturn(uriSpec);
    when(uriSpec.uri(IPAddress + "/country")).thenReturn(headersSpec);
    when(headersSpec.retrieve()).thenReturn(responseSpec);

    when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(countryIsoCode));

    String result = geoIpCacheService.fetchCountry(IPAddress);

    assertEquals(countryIsoCode, result);

    verify(webClient).get();
    verify(uriSpec).uri(IPAddress + "/country");
    verify(headersSpec).retrieve();
    verify(responseSpec).bodyToMono(String.class);
  }
}
