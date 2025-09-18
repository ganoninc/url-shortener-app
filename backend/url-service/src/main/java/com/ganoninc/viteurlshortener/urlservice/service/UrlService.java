package com.ganoninc.viteurlshortener.urlservice.service;

import com.ganoninc.viteurlshortener.urlservice.dto.UserUrlDTO;
import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;

import java.util.List;
import java.util.Optional;

public interface UrlService {
    UrlMapping createUrlMapping(String originalUrl, String userEmail);

    List<UrlMapping> getUserUrls(String userEmail);

    Optional<UserUrlDTO> getUserUrl(String shortId);
}
