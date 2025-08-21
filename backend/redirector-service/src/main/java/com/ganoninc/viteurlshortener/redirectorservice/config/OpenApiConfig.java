package com.ganoninc.viteurlshortener.redirectorservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "Vite URL Shortener - Redirector Service", version = "1.0")
)
public class OpenApiConfig {
}
