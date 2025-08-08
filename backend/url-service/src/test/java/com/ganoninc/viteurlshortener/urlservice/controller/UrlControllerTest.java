package com.ganoninc.viteurlshortener.urlservice.controller;

import com.ganoninc.viteurlshortener.urlservice.dto.ShortenURLRequestDTO;
import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;
import com.ganoninc.viteurlshortener.urlservice.service.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlControllerTest {

    private final String testEmail = "test@test.com";

    @Mock
    private UrlService urlService;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private UrlController urlController;

    @BeforeEach
    void setUp() {
        when(jwt.getSubject()).thenReturn(testEmail);
    }

    @Test
    void itShouldReturnShortIdWhenUrlIsShortened() {
        String longUrl = "https://a-quite-long-url.com/that-could-be-shortened";
        ShortenURLRequestDTO shortenURLRequestDTO = new ShortenURLRequestDTO();
        shortenURLRequestDTO.setOriginalUrl(longUrl);

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setShortId("testId01");

        when(urlService.createUrlMapping(longUrl, jwt.getSubject())).thenReturn(urlMapping);

        ResponseEntity<?> response = urlController.shortenUrl(shortenURLRequestDTO, jwt);

        assertEquals(200, response.getStatusCode().value());
        assertInstanceOf(Map.class, response.getBody());
        assertEquals("testId01", ((Map) response.getBody()).get("shortId"));
    }

    @Test
    void itShouldReturnUrlsWhenMyUrlsIsCalled() {
        UrlMapping urlMapping1 = new UrlMapping();
        urlMapping1.setShortId("testId01");
        UrlMapping urlMapping2 = new UrlMapping();
        urlMapping2.setShortId("testId02");

        List<UrlMapping> urlMappings = List.of(urlMapping1, urlMapping2);

        when(urlService.getUserUrls(testEmail)).thenReturn(urlMappings);

        List<UrlMapping> response = urlController.getMyUrls(jwt);

        assertEquals(2, response.size());
        assertEquals(response.get(0).getShortId(), urlMapping1.getShortId());
        assertEquals(response.get(1).getShortId(), urlMapping2.getShortId());

    }
}
