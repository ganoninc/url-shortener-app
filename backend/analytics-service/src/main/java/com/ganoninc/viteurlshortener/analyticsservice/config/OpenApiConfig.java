package com.ganoninc.viteurlshortener.analyticsservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "Vite URL Shortener - Analytics Service", version = "1.0"),
    security = @SecurityRequirement(name = "bearerAuth"))
public class OpenApiConfig {}
