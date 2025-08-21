package com.ganoninc.viteurlshortener.redirectorservice.controller;

import com.ganoninc.viteurlshortener.redirectorservice.kafka.ClickEventProducer;
import com.ganoninc.viteurlshortener.redirectorservice.model.UrlMapping;
import com.ganoninc.viteurlshortener.redirectorservice.repository.UrlRepository;
import com.ganoninc.viteurlshortener.redirectorservice.util.FakeUrlMapping;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlRepository urlRepository;

    @MockitoBean
    private ClickEventProducer clickEventProducer;

    @Test
    public void itShouldRedirectToTheOriginalUrl() throws Exception {
        UrlMapping urlMapping = FakeUrlMapping.getFakeUrlMapping();
        when(urlRepository.findByShortId(urlMapping.getShortId())).thenReturn(Optional.of(urlMapping));

        mockMvc.perform(MockMvcRequestBuilders.get("/" + urlMapping.getShortId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", urlMapping.getOriginalUrl()));

        verify(urlRepository, times(1)).findByShortId(urlMapping.getShortId());
    }

    @Test
    public void itShouldReturn404WhenTheShortIdIsNotFound() throws Exception {
        String wrondShortId = "wrongId";
        when(urlRepository.findByShortId(wrondShortId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/" + wrondShortId)).andExpect(status().isNotFound());

        verify(urlRepository, times(1)).findByShortId(wrondShortId);
    }

    @Test
    public void itShouldSendAClickEventWhenTheShortIdIsFound() throws Exception {
        UrlMapping urlMapping = FakeUrlMapping.getFakeUrlMapping();
        when(urlRepository.findByShortId(urlMapping.getShortId())).thenReturn(Optional.of(urlMapping));

        mockMvc.perform(MockMvcRequestBuilders.get("/" + urlMapping.getShortId())
                .header("User-Agent", "Mozilla/5.0")
                .with(request -> {
                        request.setRemoteAddr("192.281.21.222");
                        return request;
                    }
                ));

        verify(clickEventProducer, times(1)).sendClickEvent(urlMapping.getShortId(), "192.281.21.222", "Mozilla/5.0");
    }

    @Test
    public void itShouldNotSendAClickEventWhenTheShortIdIsNotFound() throws Exception {
        String wrondShortId = "wrongId";
        when(urlRepository.findByShortId(wrondShortId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/" + wrondShortId));

        verify(clickEventProducer, times(0)).sendClickEvent(any(), any(), any());
    }
}
