package com.ganoninc.viteurlshortener.redirectorservice.util;

import com.ganoninc.viteurlshortener.redirectorservice.model.UrlMapping;

import java.util.Date;

public class FakeUrlMapping {
  public static UrlMapping getFakeUrlMapping() {
    UrlMapping urlMapping = new UrlMapping();
    urlMapping.setId(1L);
    urlMapping.setOriginalUrl("http://google.com");
    urlMapping.setCreatedAt(new Date().toInstant());
    urlMapping.setShortId("abcd");
    urlMapping.setUserEmail("user@test.com");

    return urlMapping;
  }
}
