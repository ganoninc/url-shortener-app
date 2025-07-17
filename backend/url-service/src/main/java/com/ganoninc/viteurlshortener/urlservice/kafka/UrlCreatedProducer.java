package com.ganoninc.viteurlshortener.urlservice.kafka;

import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;

public interface UrlCreatedProducer {
    void sendUrlCreated(UrlMapping urlMapping);
}
