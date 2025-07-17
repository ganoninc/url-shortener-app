package com.ganoninc.viteurlshortener.urlservice.service;

import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;

import java.util.List;

public interface UrlService {
    UrlMapping createUrlMapping(String originalUrl, String userEmail);

    List<UrlMapping> getUserUrls(String userEmail);
}
