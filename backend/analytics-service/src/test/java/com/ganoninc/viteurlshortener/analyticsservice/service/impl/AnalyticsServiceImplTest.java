package com.ganoninc.viteurlshortener.analyticsservice.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ganoninc.viteurlshortener.analyticsservice.model.ClickEvent;
import com.ganoninc.viteurlshortener.analyticsservice.repository.ClickRepository;
import com.ganoninc.viteurlshortener.analyticsservice.service.PaginatedClickEvents;
import com.ganoninc.viteurlshortener.analyticsservice.util.FakeClickEvent;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceImplTest {

  private final String shortId = "shortId";
  private final String wrongShortId = "wrongShortId";

  @InjectMocks AnalyticsServiceImpl analyticsServiceImpl;
  @Mock ClickRepository clickRepository;

  @Test
  public void itShouldGetClickCountOfShortId() {
    List<ClickEvent> events = FakeClickEvent.getListOfFakeClickEvent();
    when(clickRepository.countByShortId(shortId)).thenReturn((long) events.size());

    long clickCount = analyticsServiceImpl.getClickCount(shortId);

    assertEquals(events.size(), clickCount);
    verify(clickRepository).countByShortId(shortId);
  }

  @Test
  public void itShouldReturnAllClickEventsWhenRowCountIsLessThanSize() {
    List<ClickEvent> events = FakeClickEvent.getListOfFakeClickEvent();
    when(clickRepository.findByShortIdOrderByIdDesc(eq(shortId), any(Pageable.class)))
        .thenReturn(events);
    when(clickRepository.countByShortId(shortId)).thenReturn((long) events.size());

    PaginatedClickEvents paginatedClickEvents =
        analyticsServiceImpl.getClickEvents(shortId, null, 50);

    assertEquals(events.size(), paginatedClickEvents.events().items().size());
    assertEquals(events.size(), paginatedClickEvents.totalClicks());
    verify(clickRepository).findByShortIdOrderByIdDesc(eq(shortId), any(Pageable.class));
  }

  @Test
  public void itShouldReturnANextCursorWhenRowCountIsMoreThanSize() {
    List<ClickEvent> events = FakeClickEvent.getListOfFakeClickEvent();
    int pageSize = 5;
    when(clickRepository.findByShortIdOrderByIdDesc(eq(shortId), any(Pageable.class)))
        .thenReturn(events);
    when(clickRepository.countByShortId(shortId)).thenReturn((long) events.size());

    PaginatedClickEvents paginatedClickEvents =
        analyticsServiceImpl.getClickEvents(shortId, null, pageSize);

    assertNotNull(paginatedClickEvents.events().nextCursor());
    assertEquals(events.get(pageSize - 1).getId(), paginatedClickEvents.events().nextCursor());
  }

  @Test
  public void itShouldReturnASliceOfEventsStartingAfterCursor() {
    int pageSize = 5;
    List<ClickEvent> events = FakeClickEvent.getListOfFakeClickEvent();
    Collections.reverse(events);

    long cursor = events.get(pageSize - 1).getId();
    List<ClickEvent> eventsAfterCursor = events.stream().filter(e -> e.getId() < cursor).toList();

    when(clickRepository.findAfterCursorByShortId(eq(shortId), eq(cursor), any(Pageable.class)))
        .thenReturn(eventsAfterCursor);
    when(clickRepository.countByShortId(shortId)).thenReturn((long) events.size());

    PaginatedClickEvents paginatedClickEvents =
        analyticsServiceImpl.getClickEvents(shortId, cursor, pageSize);

    assertTrue(cursor > paginatedClickEvents.events().items().get(0).id());
    verify(clickRepository).findAfterCursorByShortId(eq(shortId), eq(cursor), any(Pageable.class));
  }

  @Test
  public void itShouldReturnZeroClickCountForWrongShortId() {
    when(clickRepository.countByShortId(wrongShortId)).thenReturn(0L);

    long count = analyticsServiceImpl.getClickCount(wrongShortId);

    assertEquals(0, count);
    verify(clickRepository).countByShortId(wrongShortId);
  }

  @Test
  public void itShouldReturnZeroEventsForWrongShortId() {
    when(clickRepository.findByShortIdOrderByIdDesc(eq(wrongShortId), any(Pageable.class)))
        .thenReturn(List.of());

    PaginatedClickEvents paginatedClickEvents =
        analyticsServiceImpl.getClickEvents(wrongShortId, null, 50);

    assertEquals(0, paginatedClickEvents.events().items().size());
    assertEquals(0, paginatedClickEvents.totalClicks());
    verify(clickRepository).findByShortIdOrderByIdDesc(eq(wrongShortId), any(Pageable.class));
  }
}
