package com.ganoninc.viteurlshortener.urlservice.dto;

import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response containing the shortened URL ID")
public record ShortenURLResponseDTO(
    @Schema(description = "The shortened identifier for the URL", example = "abc123")
    String shortId
) {
    public static ShortenURLResponseDTO from(UrlMapping urlMapping) {
        return new ShortenURLResponseDTO(urlMapping.getShortId());
    }
}