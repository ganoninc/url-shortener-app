package com.ganoninc.viteurlshortener.urlservice.controller;

import com.ganoninc.viteurlshortener.urlservice.common.UserContext;
import com.ganoninc.viteurlshortener.urlservice.dto.ShortenURLRequestDTO;
import com.ganoninc.viteurlshortener.urlservice.dto.ShortenURLResponseDTO;
import com.ganoninc.viteurlshortener.urlservice.dto.UserUrlDTO;
import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;
import com.ganoninc.viteurlshortener.urlservice.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    @Operation(
            summary = "Shorten a URL",
            description = "Creates a shortened URL for the provided original URL",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully created short URL",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ShortenURLResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "User not logged in"
                    )
            }
    )
    public ResponseEntity<ShortenURLResponseDTO> shortenUrl(@RequestBody @Valid ShortenURLRequestDTO requestDTO) {
        String url = requestDTO.originalUrl();
        String userEmail = UserContext.getUserEmail();
        if (userEmail == null) {
            return ResponseEntity.badRequest().build();
        }

        UrlMapping urlMapping = urlService.createUrlMapping(url, userEmail);
        return ResponseEntity.ok(ShortenURLResponseDTO.from(urlMapping));
    }

    @GetMapping("/my-urls")
    @Operation(
            summary = "Get user's URLs",
            description = "Returns the list of shortened URL created by the user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of user's URL",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserUrlDTO.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "User not logged in"
                    )
            }
    )
    public ResponseEntity<List<UserUrlDTO>> getUserUrls() {
        String userEmail = UserContext.getUserEmail();
        if (userEmail == null) {
            return ResponseEntity.badRequest().build();
        }

        List<UserUrlDTO> urls = urlService.getUserUrls(userEmail)
                .stream()
                .map(UserUrlDTO::from)
                .toList();

        return ResponseEntity.ok(urls);
    }
}
