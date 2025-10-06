package com.ganoninc.viteurlshortener.analyticsservice.controller;

import com.ganoninc.viteurlshortener.analyticsservice.model.ClickEvent;
import com.ganoninc.viteurlshortener.analyticsservice.service.AnalyticsService;
import com.ganoninc.viteurlshortener.analyticsservice.util.FakeClickEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalyticsController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AnalyticsControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private AnalyticsService analyticsService;

  @Test
  public void itShouldReturnStatsOfAShortenedUrl() throws Exception {
    long clickCount = 10;
    ClickEvent clickEvent = FakeClickEvent.getFakeClickEvent(1L);
    ClickEvent clickEvent2 = FakeClickEvent.getFakeClickEvent(2L);
    List<ClickEvent> listOfClickEvents = List.of(clickEvent, clickEvent2);

    when(analyticsService.getClickCount(clickEvent.getShortId())).thenReturn(clickCount);
    when(analyticsService.getAllEvents(clickEvent.getShortId())).thenReturn(listOfClickEvents);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/" + clickEvent.getShortId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.clickCount").value(clickCount))
        .andExpect(jsonPath("$.events[0].shortId").value(clickEvent.getShortId()))
        .andExpect(jsonPath("$.events[1].shortId").value(clickEvent2.getShortId()));

    verify(analyticsService, times(1)).getClickCount(clickEvent.getShortId());
    verify(analyticsService, times(1)).getAllEvents(clickEvent.getShortId());
  }
}
