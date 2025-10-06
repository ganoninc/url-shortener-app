package com.ganoninc.viteurlshortener.redirectorservice.controller;

import com.ganoninc.viteurlshortener.redirectorservice.kafka.ClickEventProducer;
import com.ganoninc.viteurlshortener.redirectorservice.model.UrlMapping;
import com.ganoninc.viteurlshortener.redirectorservice.repository.UrlRepository;
import com.ganoninc.viteurlshortener.redirectorservice.service.RedirectorService;
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

  private final String remoteAddress = "127.0.0.1";
  private final String userAgent = "Mozilla";
  private final String unknownShortId = "unknownShortId";

  @Autowired private MockMvc mockMvc;

  @MockitoBean private RedirectorService redirectorService;

  @Test
  public void itShouldRedirectToTheOriginalUrl() throws Exception {
    UrlMapping urlMapping = FakeUrlMapping.getFakeUrlMapping();
    when(redirectorService.resolveRedirect(urlMapping.getShortId(), remoteAddress, userAgent))
        .thenReturn(Optional.of(urlMapping.getOriginalUrl()));

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/" + urlMapping.getShortId())
                .with(
                    request -> {
                      request.setRemoteAddr(remoteAddress);
                      request.addHeader("User-Agent", userAgent);
                      return request;
                    }))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string("Location", urlMapping.getOriginalUrl()));

    verify(redirectorService, times(1))
        .resolveRedirect(urlMapping.getShortId(), remoteAddress, userAgent);
  }

  @Test
  public void itShouldReturn404WhenTheShortIdIsNotFound() throws Exception {
    when(redirectorService.resolveRedirect(unknownShortId, remoteAddress, userAgent))
        .thenReturn(Optional.empty());
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/" + unknownShortId)
                .with(
                    request -> {
                      request.setRemoteAddr(remoteAddress);
                      request.addHeader("User-Agent", userAgent);
                      return request;
                    }))
        .andExpect(status().isNotFound());

    verify(redirectorService, times(1)).resolveRedirect(unknownShortId, remoteAddress, userAgent);
  }
}
