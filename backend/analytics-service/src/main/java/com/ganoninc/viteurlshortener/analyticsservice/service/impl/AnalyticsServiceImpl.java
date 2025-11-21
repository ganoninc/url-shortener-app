package com.ganoninc.viteurlshortener.analyticsservice.service.impl;

import com.ganoninc.viteurlshortener.analyticsservice.common.PaginatedSlice;
import com.ganoninc.viteurlshortener.analyticsservice.model.ClickEvent;
import com.ganoninc.viteurlshortener.analyticsservice.repository.ClickRepository;
import com.ganoninc.viteurlshortener.analyticsservice.service.AnalyticsService;
import com.ganoninc.viteurlshortener.analyticsservice.service.ClickEventView;
import com.ganoninc.viteurlshortener.analyticsservice.service.PaginatedClickEvents;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

  private final ClickRepository clickRepository;

  public AnalyticsServiceImpl(ClickRepository clickRepository) {
    this.clickRepository = clickRepository;
  }

  @Override
  public long getClickCount(String shortId) {
    return clickRepository.countByShortId(shortId);
  }

  @Override
  public PaginatedClickEvents getClickEvents(String shortId, Long cursor, int size) {
    List<ClickEvent> eventEntities;

    if (cursor == null) {
      eventEntities = clickRepository.findByShortIdOrderByIdAsc(shortId, Pageable.ofSize(size + 1));
    } else {
      eventEntities =
          clickRepository.findAfterCursorByShortId(shortId, cursor, Pageable.ofSize(size + 1));
    }

    Long nextCursor = null;
    if (eventEntities.size() > size) {
      nextCursor = eventEntities.get(size - 1).getId();
      eventEntities = eventEntities.subList(0, size);
    }

    PaginatedSlice<ClickEventView, Long> clickEventViews =
        new PaginatedSlice<>(eventEntities.stream().map(ClickEventView::from).toList(), nextCursor);

    return new PaginatedClickEvents(clickRepository.countByShortId(shortId), clickEventViews);
  }
}
