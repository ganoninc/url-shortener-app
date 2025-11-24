package com.ganoninc.viteurlshortener.analyticsservice.dto;

import com.ganoninc.viteurlshortener.analyticsservice.service.ClickEventView;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "Represents a single click event associated with a shortened URL")
public record ClickEventDTO(
    @Schema(
            description = "Unique database identifier for the click event",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "42")
        Long id,
    @Schema(
            description = "When the click occurred (UTC)",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "2025-01-20T14:23:11Z")
        Instant timestamp,
    @Schema(
            description = "User agent string of the requester",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)")
        String userAgent,
    @Schema(
            description =
                "ISO country code resolved from the requester's IP address. '-E' indicates that the country could not be determined.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "FR")
        String countryCode) {
  public static ClickEventDTO from(ClickEventView clickEventView) {
    return new ClickEventDTO(
        clickEventView.id(),
        clickEventView.timestamp(),
        clickEventView.userAgent(),
        clickEventView.countryCode());
  }
}
