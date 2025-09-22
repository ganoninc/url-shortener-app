package com.ganoninc.viteurlshortener.urlservice.dto;
import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Response containing the shortened URL id, the original URL, and its creation date")
public record UserUrlDTO(
        @Schema(description = "The shortened identifier of the URL", example = "abc123", requiredMode = Schema.RequiredMode.REQUIRED)
        String shortId,
        @Schema(description = "The original URL", example = "https://www.example.com/my-article", requiredMode = Schema.RequiredMode.REQUIRED)
        String originalUrl,
        @Schema(description = "The creation date", example = "2011-12-03T10:15:30Z", requiredMode = Schema.RequiredMode.REQUIRED)
        Instant createdAt
) { public static UserUrlDTO from(UrlMapping urlMapping) {
    return new UserUrlDTO(urlMapping.getShortId(), urlMapping.getOriginalUrl(), urlMapping.getCreatedAt());
}}