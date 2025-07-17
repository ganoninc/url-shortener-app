package com.ganoninc.viteurlshortener.urlservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "Vite URL Shortener - URL Service", version = "1.0"),
    security = @SecurityRequirement(name = "bearerAuth")
)
public class OpenApiConfig {
}
