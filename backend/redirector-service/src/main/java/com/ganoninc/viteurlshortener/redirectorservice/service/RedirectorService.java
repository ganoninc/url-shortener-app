package com.ganoninc.viteurlshortener.redirectorservice.service;

import java.util.Optional;

public interface RedirectorService {
  Optional<String> resolveRedirect(String shortId, String ip, String userAgent);
}
