package com.ganoninc.viteurlshortener.analyticsservice.dto;

import com.ganoninc.viteurlshortener.analyticsservice.common.PaginatedSlice;
import com.ganoninc.viteurlshortener.analyticsservice.service.PaginatedClickEvents;
import io.swagger.v3.oas.annotations.media.Schema;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Schema(
    description =
        "Aggregated statistics for a shortened URL, including total clicks and event history")
public record ShortUrlClickEventsDTO(
    @Schema(
            description = "Total number of recorded clicks",
            example = "201041",
            requiredMode = Schema.RequiredMode.REQUIRED)
        long totalClicks,
    @Schema(
            description =
                """
        Cursor-paginated window of click events for this short URL.
        This list contains only the events included in the current slice,
        ordered from newest to oldest. The number of items is limited by
        the requested page size. Use nextCursor in the PaginatedSlice to
        retrieve the following window of events.
        """,
            requiredMode = Schema.RequiredMode.REQUIRED)
        PaginatedSlice<ClickEventDTO, String> events) {

  public static ShortUrlClickEventsDTO from(PaginatedClickEvents paginatedClickEvents) {
    var clickEventDtoList =
        paginatedClickEvents.events().items().stream().map(ClickEventDTO::from).toList();

    String nextCursor = null;
    if (paginatedClickEvents.events().nextCursor() != null) {
      nextCursor =
          Base64.getUrlEncoder()
              .encodeToString(
                  String.valueOf(paginatedClickEvents.events().nextCursor())
                      .getBytes(StandardCharsets.UTF_8));
    }

    PaginatedSlice<ClickEventDTO, String> paginatedClickEventsDto =
        new PaginatedSlice<>(clickEventDtoList, nextCursor);

    return new ShortUrlClickEventsDTO(paginatedClickEvents.totalClicks(), paginatedClickEventsDto);
  }
}
