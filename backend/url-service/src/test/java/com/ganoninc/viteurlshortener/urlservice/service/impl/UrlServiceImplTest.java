package com.ganoninc.viteurlshortener.urlservice.service.impl;

import com.ganoninc.viteurlshortener.urlservice.kafka.UrlCreatedProducer;
import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;
import com.ganoninc.viteurlshortener.urlservice.repository.UrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private UrlCreatedProducer urlCreatedProducer;

    @InjectMocks
    private UrlServiceImpl urlService;

    private final String testEmail = "test@test.com";
    private final String originalUrl = "https://a-quite-long-url.com/that-could-be-shortened";

    @Test
    void itShouldCreateUrlMappingAndSendEvent() {
        ArgumentCaptor<UrlMapping> captor = ArgumentCaptor.forClass(UrlMapping.class);

        UrlMapping saved = new UrlMapping();
        saved.setId(1L);
        saved.setShortId("abc12345");
        saved.setOriginalUrl(originalUrl);
        saved.setUserEmail(testEmail);

        when(urlRepository.save(any(UrlMapping.class))).thenReturn(saved);

        UrlMapping result = urlService.createUrlMapping(originalUrl, testEmail);

        verify(urlRepository).save(captor.capture());
        verify(urlCreatedProducer).sendUrlCreated(saved);

        UrlMapping toSave = captor.getValue();
        assertEquals(originalUrl, toSave.getOriginalUrl());
        assertEquals(testEmail, toSave.getUserEmail());
        assertNotNull(toSave.getShortId());
        assertEquals(8, toSave.getShortId().length());

        assertEquals(saved, result);
    }

    @Test
    void itShouldReturnUserUrls() {
        UrlMapping url1 = new UrlMapping();
        url1.setShortId("short1");
        UrlMapping url2 = new UrlMapping();
        url2.setShortId("short2");

        when(urlRepository.findAllByUserEmail(testEmail)).thenReturn(List.of(url1, url2));

        List<UrlMapping> result = urlService.getUserUrls(testEmail);

        assertEquals(2, result.size());
        assertEquals("short1", result.get(0).getShortId());
        assertEquals("short2", result.get(1).getShortId());
        verify(urlRepository).findAllByUserEmail(testEmail);
    }
}