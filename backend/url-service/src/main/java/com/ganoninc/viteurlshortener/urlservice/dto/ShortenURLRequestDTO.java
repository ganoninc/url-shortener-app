package com.ganoninc.viteurlshortener.urlservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortenURLRequestDTO {
    @Schema(description = "Long URL to shorten", example = "https://www.example.com/my-article")
    @NotBlank(message = "Original URL must not be blank")
    @Pattern(
        regexp = "^(https?)://[^\\s/$.?#].\\S*$",
        message = "Invalid URL format"
    )
    private String originalUrl;
}
