package com.ganoninc.viteurlshortener.urlservice.service.impl;

import com.ganoninc.viteurlshortener.urlservice.kafka.UrlCreatedProducer;
import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;
import com.ganoninc.viteurlshortener.urlservice.repository.UrlRepository;
import com.ganoninc.viteurlshortener.urlservice.service.UrlService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final UrlCreatedProducer urlCreatedProducer;

    public UrlServiceImpl(UrlRepository repository, UrlCreatedProducer producer){
        this.urlRepository = repository;
        this.urlCreatedProducer = producer;
    }


    @Override
    public UrlMapping createUrlMapping(String originalUrl, String userEmail) {
        String shortId = UUID.randomUUID().toString().substring(0, 8);
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortId(shortId);
        urlMapping.setUserEmail(userEmail);

        UrlMapping savedEntity = urlRepository.save(urlMapping);
        urlCreatedProducer.sendUrlCreated(savedEntity);

        return savedEntity;
    }

    @Override
    public List<UrlMapping> getUserUrls(String userEmail) {
        return urlRepository.findAllByUserEmail(userEmail);
    }
}
