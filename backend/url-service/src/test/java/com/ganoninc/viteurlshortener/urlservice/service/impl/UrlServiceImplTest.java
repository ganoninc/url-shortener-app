package com.ganoninc.viteurlshortener.urlservice.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ganoninc.viteurlshortener.urlservice.dto.UserUrlDTO;
import com.ganoninc.viteurlshortener.urlservice.kafka.UrlCreatedProducer;
import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;
import com.ganoninc.viteurlshortener.urlservice.repository.UrlRepository;
import com.ganoninc.viteurlshortener.urlservice.util.FakeUrlMapping;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {

    private final String userEmail = "test@test.com";
    private final String originalUrl = "https://a-quite-long-url.com/that-could-be-shortened";
    @Mock
    private UrlRepository urlRepository;
    @Mock
    private UrlCreatedProducer urlCreatedProducer;
    @InjectMocks
    private UrlServiceImpl urlService;

    @Test
    void itShouldCreateUrlMappingAndSendEvent() {
        ArgumentCaptor<UrlMapping> captor = ArgumentCaptor.forClass(UrlMapping.class);

        UrlMapping saved = FakeUrlMapping.builder().shortId("abcd1234").originalUrl(originalUrl).userEmail(userEmail).build();

        when(urlRepository.save(any(UrlMapping.class))).thenReturn(saved);

        UrlMapping result = urlService.createUrlMapping(originalUrl, userEmail);

        verify(urlRepository).save(captor.capture());
        verify(urlCreatedProducer).sendUrlCreated(saved);

        UrlMapping toSave = captor.getValue();
        assertEquals(originalUrl, toSave.getOriginalUrl());
        assertEquals(userEmail, toSave.getUserEmail());
        assertNotNull(toSave.getShortId());
        assertEquals(8, toSave.getShortId().length());

        assertEquals(saved, result);
    }

    @Test
    void itShouldReturnUserUrls() {
        UrlMapping url1 = FakeUrlMapping.builder().shortId("abc12345").build();
        UrlMapping url2 = FakeUrlMapping.builder().shortId("abc12346").build();

        when(urlRepository.findAllByUserEmail(userEmail)).thenReturn(List.of(url1, url2));

        List<UrlMapping> result = urlService.getUserUrls(userEmail);

        assertEquals(2, result.size());
        assertEquals(url1.getShortId(), result.get(0).getShortId());
        assertEquals(url2.getShortId(), result.get(1).getShortId());
        verify(urlRepository).findAllByUserEmail(userEmail);
    }

    @Test
    void itShouldReturnUserUrlDetailsWithValidShortId() {
        UrlMapping url = FakeUrlMapping.builder().userEmail(userEmail).originalUrl(originalUrl).build();
        UserUrlDTO urlDto = UserUrlDTO.from(url);

        when(urlRepository.findByShortId(url.getShortId())).thenReturn(Optional.of(url));

        Optional<UserUrlDTO> result = urlService.getUserUrl(url.getShortId());

        assertEquals(urlDto, result.get());
        verify(urlRepository).findByShortId(url.getShortId());
    }

    @Test
    void itShouldReturnAnEmptyOptionalWhenUrlMappingNotFound() {
        String unknowShortId = "unknow-short-id";
        when(urlRepository.findByShortId(unknowShortId)).thenReturn(Optional.empty());

        Optional<UserUrlDTO> result = urlService.getUserUrl(unknowShortId);

        assertTrue(result.isEmpty());
        verify(urlRepository).findByShortId(unknowShortId);
    }
}