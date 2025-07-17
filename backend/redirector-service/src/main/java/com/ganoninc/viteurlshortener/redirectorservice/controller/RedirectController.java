package com.ganoninc.viteurlshortener.redirectorservice.controller;

import com.ganoninc.viteurlshortener.redirectorservice.kafka.ClickEventProducer;
import com.ganoninc.viteurlshortener.redirectorservice.repository.UrlRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class RedirectController {
    private final UrlRepository urlRepository;
    private final ClickEventProducer clickEventProducer;

    public RedirectController(UrlRepository urlRepository, ClickEventProducer clickEventProducer) {
        this.urlRepository = urlRepository;
        this.clickEventProducer = clickEventProducer;
    }


    @GetMapping("/{shortId}")
    public ResponseEntity<Object> redirect(@PathVariable String shortId,
                                           HttpServletRequest request) {
        return urlRepository.findByShortId(shortId)
                .map(mapping -> {
                    clickEventProducer.sendClickEvent(
                        shortId,
                        request.getRemoteAddr(),
                        request.getHeader("User-Agent")
                    );
                    return ResponseEntity.status(HttpStatus.FOUND)
                            .location(URI.create(mapping.getOriginalUrl()))
                            .build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
