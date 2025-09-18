package com.ganoninc.viteurlshortener.urlservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ganoninc.viteurlshortener.urlservice.config.SecurityConfig;
import com.ganoninc.viteurlshortener.urlservice.dto.UserUrlDTO;
import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;
import com.ganoninc.viteurlshortener.urlservice.service.UrlService;
import com.ganoninc.viteurlshortener.urlservice.util.FakeUrlMapping;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UrlController.class)
@Import({SecurityConfig.class})
public class UrlControllerTest {

    private final ObjectMapper objectMapper;
    private final String userEmail = "user@test.com";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlService urlService;

    public UrlControllerTest() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void itShouldReturnAShortIdWhenAUrlIsShortened() throws Exception {
        UrlMapping urlMapping = FakeUrlMapping.builder().build();
        Map<String, String> queryBody = Map.of("originalUrl", urlMapping.getOriginalUrl());

        when(urlService.createUrlMapping(urlMapping.getOriginalUrl(), urlMapping.getUserEmail())).thenReturn(urlMapping);

        mockMvc.perform(MockMvcRequestBuilders.post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(queryBody))
                .header("X-User-Sub",urlMapping.getUserEmail()))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"shortId\":\"" + urlMapping.getShortId() +"\"}"));

        verify(urlService, times(1)).createUrlMapping(urlMapping.getOriginalUrl(), urlMapping.getUserEmail());
    }

    @Test
    void itShouldNotCallCreateUrlMappingWhenAWrongUrlIsSubmited() throws Exception {
        String aWrongUrl = "a-http:wrong//url.com";
        Map<String, String> queryBody = Map.of("originalUrl", aWrongUrl);

        mockMvc.perform(MockMvcRequestBuilders.post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(queryBody))
                .header("X-User-Sub",userEmail))
                .andExpect(status().isBadRequest());

        verify(urlService, never()).createUrlMapping(any(), any());
    }

    @Test
    void itShouldNotCallCreateUrlMappingWhenTheRequestBodyIsEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                .header("X-User-Sub",userEmail))
                .andExpect(status().isBadRequest());

        verify(urlService, never()).createUrlMapping(any(), any());
    }

    @Test
    void itShouldReturnAListOfUrlsWhenMyUrlsIsCalled() throws Exception {
        UrlMapping urlMapping1 = FakeUrlMapping.builder().build();
        UrlMapping urlMapping2 = FakeUrlMapping.builder().build();
        List<UrlMapping> urlMappings = List.of(urlMapping1, urlMapping2);
        List<UserUrlDTO> urlDtos = urlMappings
                .stream()
                .map(UserUrlDTO::from)
                .toList();

        when(urlService.getUserUrls(urlMapping1.getUserEmail())).thenReturn(urlMappings);

        mockMvc.perform(MockMvcRequestBuilders.get("/my-urls")
                .header("X-User-Sub", urlMapping1.getUserEmail()))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(urlDtos)));
    }

    @Test
    void itShouldReturnAnEmptyListOfUrlsWhenMyUrlsIsCalledAndTheUserHasNoUrls() throws Exception {
        List<UrlMapping> urlMappings = List.of();

        when(urlService.getUserUrls(userEmail)).thenReturn(urlMappings);

        mockMvc.perform(MockMvcRequestBuilders.get("/my-urls")
                .header("X-User-Sub", userEmail))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(urlMappings)));
    }

    @Test
    void itShouldReturnDetailsOfAShortenedUrlWhenMyUrlsWithShortIdIsCalled() throws Exception {
        UrlMapping urlMapping1 = FakeUrlMapping.builder().build();
        UserUrlDTO userUrlDTO = UserUrlDTO.from(urlMapping1);

        when(urlService.getUserUrl(urlMapping1.getShortId())).thenReturn(Optional.of(userUrlDTO));

        mockMvc.perform((MockMvcRequestBuilders.get("/my-urls/" + urlMapping1.getShortId())
                .header("X-User-Sub", urlMapping1.getUserEmail())))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(userUrlDTO)));
    }

    @Test
    void itShouldReturn404NotFoundWhenMyUrlsWithShortIdIsCalledAndThereIsRecord() throws Exception {
        when(urlService.getUserUrl("unknowShortId")).thenReturn(Optional.empty());

        mockMvc.perform((MockMvcRequestBuilders.get("/my-urls/unknowShortId")
                        .header("X-User-Sub", userEmail)))
                .andExpect(status().isNotFound());
    }
}
