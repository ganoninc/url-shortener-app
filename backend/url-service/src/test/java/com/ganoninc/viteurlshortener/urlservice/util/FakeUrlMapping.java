package com.ganoninc.viteurlshortener.urlservice.util;

import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class FakeUrlMapping {
    private static final AtomicInteger counter = new AtomicInteger(0);

    private String originalUrl = "https://www.a-very-long-url.com";
    private String shortId = "abcd";
    private String userEmail = "test@test.com";
    private Instant createdAt = Instant.now();


    public static FakeUrlMapping builder() {
        return new FakeUrlMapping();
    }

    public FakeUrlMapping originalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
        return this;
    }

    public FakeUrlMapping shortId(String shortId) {
        this.shortId = shortId;
        return this;
    }

    public FakeUrlMapping userEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public FakeUrlMapping createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public UrlMapping build() {
        int id = counter.incrementAndGet();
        if(shortId.equals("abcd")) {
            shortId += Integer.toString(id);
        }

        return new UrlMapping(null, originalUrl, shortId, userEmail, createdAt);
    }
}
