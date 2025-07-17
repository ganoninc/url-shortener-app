package com.ganoninc.viteurlshortener.urlservice.kafka.impl;

import com.ganoninc.viteurlshortener.urlservice.kafka.UrlCreatedProducer;
import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;

import org.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UrlCreateProducerImpl implements UrlCreatedProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public UrlCreateProducerImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendUrlCreated(UrlMapping urlMapping) {
        String payload = new JSONObject()
                .put("shortId", urlMapping.getShortId())
                .put("originalUrl", urlMapping.getOriginalUrl())
                .put("userEmail", urlMapping.getUserEmail())
                .toString();

        kafkaTemplate.send("url-created", payload);
    }
}
