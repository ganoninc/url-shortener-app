package com.ganoninc.viteurlshortener.redirectorservice.service.impl;

import com.ganoninc.viteurlshortener.redirectorservice.kafka.ClickEventProducer;
import com.ganoninc.viteurlshortener.redirectorservice.model.UrlMapping;
import com.ganoninc.viteurlshortener.redirectorservice.repository.UrlRepository;
import com.ganoninc.viteurlshortener.redirectorservice.service.RedirectorService;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class RedirectorServiceImpl implements RedirectorService {
  private final UrlRepository urlRepository;
  private final ClickEventProducer clickEventProducer;

  public RedirectorServiceImpl(UrlRepository urlRepository, ClickEventProducer clickEventProducer) {
    this.urlRepository = urlRepository;
    this.clickEventProducer = clickEventProducer;
  }

  @WithSpan("redirect.resolve")
  public Optional<String> resolveRedirect(
      @SpanAttribute("url.short_id") String shortId,
      String ip,
      String userAgent) {
    Span currentSpan = Span.current();
    Optional<UrlMapping> urlMapping = urlRepository.findByShortId(shortId);

    urlMapping.ifPresent(
        mapping ->
            currentSpan.addEvent(
                "url.mapping.found",
                Attributes.of(AttributeKey.longKey("url.mapping.id"), mapping.getId())));

    if (urlMapping.isEmpty()) {
      currentSpan.addEvent("url.mapping.not_found");
    }

    return urlMapping.map(
        mapping -> {
          clickEventProducer.sendClickEvent(shortId, ip, userAgent);
          return mapping.getOriginalUrl();
        });
  }
}
