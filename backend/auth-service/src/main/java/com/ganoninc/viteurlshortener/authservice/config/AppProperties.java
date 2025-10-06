package com.ganoninc.viteurlshortener.authservice.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "app")
@Validated
@Getter
@Setter
public class AppProperties {
  @NotBlank
  @Pattern(regexp = "^https?://([a-zA-Z0-9.-]+)(:\\d+)?$")
  private String frontendOrigin;
}
