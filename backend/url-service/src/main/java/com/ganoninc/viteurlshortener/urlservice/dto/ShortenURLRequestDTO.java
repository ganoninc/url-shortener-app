package com.ganoninc.viteurlshortener.urlservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;


@Schema(description = "Request containing the original URL to be shortened")
public record ShortenURLRequestDTO(
    @Schema(description = "Long URL to shorten", example = "https://www.example.com/my-article", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Original URL must not be blank")
    @URL(message = "Invalid URL format")
    String originalUrl){
}
