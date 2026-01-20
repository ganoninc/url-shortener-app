package com.ganoninc.viteurlshortener.analyticsservice.service.impl;

import com.ganoninc.viteurlshortener.analyticsservice.common.PaginatedSlice;
import com.ganoninc.viteurlshortener.analyticsservice.model.ClickEvent;
import com.ganoninc.viteurlshortener.analyticsservice.repository.ClickRepository;
import com.ganoninc.viteurlshortener.analyticsservice.service.AnalyticsService;
import com.ganoninc.viteurlshortener.analyticsservice.service.ClickEventView;
import com.ganoninc.viteurlshortener.analyticsservice.service.PaginatedClickEvents;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

  private final ClickRepository clickRepository;

  public AnalyticsServiceImpl(ClickRepository clickRepository) {
    this.clickRepository = clickRepository;
  }

  @WithSpan("analytics.click_count")
  @Override
  public long getClickCount(@SpanAttribute("url.short_id") String shortId) {
    return clickRepository.countByShortId(shortId);
  }

  @Override
  public PaginatedClickEvents getClickEvents(
      @SpanAttribute("url.short_id") String shortId,
      Long cursor,
      @SpanAttribute("query.size") int size) {
    Span currentSpan = Span.current();
    currentSpan.setAttribute("has_cursor", cursor != null);
    List<ClickEvent> eventEntities;

    if (cursor == null) {
      eventEntities =
          clickRepository.findByShortIdOrderByIdDesc(shortId, Pageable.ofSize(size + 1));
    } else {
      eventEntities =
          clickRepository.findAfterCursorByShortId(shortId, cursor, Pageable.ofSize(size + 1));
    }

    if (eventEntities.isEmpty()) {
      currentSpan.addEvent("click_events.not_found");
    }

    Long nextCursor = null;
    if (eventEntities.size() > size) {
      nextCursor = eventEntities.get(size - 1).getId();
      eventEntities = eventEntities.subList(0, size);
      currentSpan.addEvent("click_events.next_cursor_added");
    }

    PaginatedSlice<ClickEventView, Long> clickEventViews =
        new PaginatedSlice<>(eventEntities.stream().map(ClickEventView::from).toList(), nextCursor);

    long totalCount = clickRepository.countByShortId(shortId);

    currentSpan.setAttribute("returned_size", clickEventViews.items().size());
    currentSpan.setAttribute("total_count", totalCount);

    return new PaginatedClickEvents(totalCount, clickEventViews);
  }
}
