package com.ganoninc.viteurlshortener.analyticsservice.controller;

import static com.ganoninc.viteurlshortener.analyticsservice.util.FakeClickEvent.getListOfFakeClickEvent;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ganoninc.viteurlshortener.analyticsservice.common.PaginatedSlice;
import com.ganoninc.viteurlshortener.analyticsservice.model.ClickEvent;
import com.ganoninc.viteurlshortener.analyticsservice.service.AnalyticsService;
import com.ganoninc.viteurlshortener.analyticsservice.service.ClickEventView;
import com.ganoninc.viteurlshortener.analyticsservice.service.PaginatedClickEvents;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(AnalyticsController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AnalyticsControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private AnalyticsService analyticsService;

  @Test
  public void itShouldReturnStatsWithoutCursor() throws Exception {
    List<ClickEvent> events = getListOfFakeClickEvent();
    String shortId = events.get(0).getShortId();

    PaginatedSlice<ClickEventView, Long> paginatedSliceOfClickEvents =
        new PaginatedSlice<>(events.stream().map(ClickEventView::from).toList(), null);
    PaginatedClickEvents response =
        new PaginatedClickEvents(events.size(), paginatedSliceOfClickEvents);

    when(analyticsService.getClickEvents(shortId, null, 50)).thenReturn(response);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/short-url/" + shortId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalClicks").value(response.totalClicks()))
        .andExpect(jsonPath("$.events.items[0].id").value(events.get(0).getId()))
        .andExpect(jsonPath("$.events.items[1].id").value(events.get(1).getId()));

    verify(analyticsService, times(1)).getClickEvents(shortId, null, 50);
  }

  @Test
  public void itShouldReturnStatsUsingEncodedCursor() throws Exception {
    List<ClickEvent> events = getListOfFakeClickEvent();
    Long nextCursor = 9999L;
    String encodedNextCursor =
        Base64.getEncoder().encodeToString(nextCursor.toString().getBytes(StandardCharsets.UTF_8));

    PaginatedSlice<ClickEventView, Long> slice =
        new PaginatedSlice<>(events.stream().map(ClickEventView::from).toList(), nextCursor);
    PaginatedClickEvents response = new PaginatedClickEvents(events.size(), slice);

    String shortId = events.get(0).getShortId();
    int pageSize = 5;
    Long cursor = 5L;
    String encodedCursor =
        Base64.getUrlEncoder().encodeToString(cursor.toString().getBytes(StandardCharsets.UTF_8));

    when(analyticsService.getClickEvents(shortId, cursor, pageSize)).thenReturn(response);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                "/short-url/" + shortId + "?cursor=" + encodedCursor + "&size=" + pageSize))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalClicks").value(response.totalClicks()))
        .andExpect(jsonPath("$.events.items[0].id").value(events.get(0).getId()))
        .andExpect(jsonPath("$.events.items[1].id").value(events.get(1).getId()))
        .andExpect(jsonPath("$.events.nextCursor").value(encodedNextCursor));

    verify(analyticsService).getClickEvents(shortId, cursor, pageSize);
  }

  @Test
  public void itShouldReturnsAnErrorWithAWrongEncodedCursor() throws Exception {
    String wrongEncodedCursor = "&%&$wrongEncodedCursor&%&$";

    mockMvc
        .perform(MockMvcRequestBuilders.get("/short-url/abcd123?cursor=" + wrongEncodedCursor))
        .andExpect(status().isBadRequest());
  }
}
