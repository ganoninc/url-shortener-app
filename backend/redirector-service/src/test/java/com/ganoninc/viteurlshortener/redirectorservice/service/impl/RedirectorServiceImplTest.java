package com.ganoninc.viteurlshortener.redirectorservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.ganoninc.viteurlshortener.redirectorservice.kafka.ClickEventProducer;
import com.ganoninc.viteurlshortener.redirectorservice.model.UrlMapping;
import com.ganoninc.viteurlshortener.redirectorservice.repository.UrlRepository;
import com.ganoninc.viteurlshortener.redirectorservice.util.FakeUrlMapping;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RedirectorServiceImplTest {
    private final String ipAddr = "215.248.102.32";
    private final String userAgent = "Mozilla";
    private String unknownShortId = "unknownShortId";

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private ClickEventProducer clickEventProducer;

    @InjectMocks
    private RedirectorServiceImpl redirectorService;

    @Test
    public void itShouldReturnTheOriginalUrlWhenShortIdExists() {
        UrlMapping urlMapping = FakeUrlMapping.getFakeUrlMapping();
        when(urlRepository.findByShortId(urlMapping.getShortId())).thenReturn(Optional.of(urlMapping));

        Optional<String> originalUrl = redirectorService.resolveRedirect(urlMapping.getShortId(), ipAddr, userAgent);

        verify(urlRepository, times(1)).findByShortId(urlMapping.getShortId());
        assertEquals(urlMapping.getOriginalUrl(), originalUrl.get());
    }

    @Test
    public void itShouldNotReturnTheOriginalUrlWhenShortDoesntExist() {
        when(urlRepository.findByShortId(unknownShortId)).thenReturn(Optional.empty());
        Optional<String> originalUrl = redirectorService.resolveRedirect(unknownShortId, ipAddr, userAgent);
        assertEquals(Optional.empty(), originalUrl);
    }

    @Test
    public void itShouldSendAClickEventWhenShortIdExists() {
        UrlMapping urlMapping = FakeUrlMapping.getFakeUrlMapping();
        when(urlRepository.findByShortId(urlMapping.getShortId())).thenReturn(Optional.of(urlMapping));

        redirectorService.resolveRedirect(urlMapping.getShortId(), ipAddr, userAgent);

        verify(clickEventProducer, times(1)).sendClickEvent(urlMapping.getShortId(), ipAddr, userAgent);
    }

    @Test
    public void itShouldNotSendAClickEventWhenShortIdDoesntExist() {
        when(urlRepository.findByShortId(unknownShortId)).thenReturn(Optional.empty());
        redirectorService.resolveRedirect(unknownShortId, ipAddr, userAgent);
        verify(clickEventProducer, never()).sendClickEvent(any(), any(), any());
    }
}
