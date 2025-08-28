package com.ganoninc.viteurlshortener.redirectorservice.service.impl;

import com.ganoninc.viteurlshortener.redirectorservice.kafka.ClickEventProducer;
import com.ganoninc.viteurlshortener.redirectorservice.model.UrlMapping;
import com.ganoninc.viteurlshortener.redirectorservice.repository.UrlRepository;
import com.ganoninc.viteurlshortener.redirectorservice.service.RedirectorService;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class RedirectorServiceImpl implements RedirectorService {
    private final UrlRepository urlRepository;
    private final ClickEventProducer clickEventProducer;

    public RedirectorServiceImpl(UrlRepository urlRepository, ClickEventProducer clickEventProducer) {
        this.urlRepository = urlRepository;
        this.clickEventProducer = clickEventProducer;
    }

    public Optional<String> resolveRedirect(String shortId, String ip, String userAgent) {
        Optional<UrlMapping> urlMapping = urlRepository.findByShortId(shortId);

        return urlMapping.map(mapping -> {
            clickEventProducer.sendClickEvent(shortId, ip, userAgent);
            return mapping.getOriginalUrl();
        });
    }
}
