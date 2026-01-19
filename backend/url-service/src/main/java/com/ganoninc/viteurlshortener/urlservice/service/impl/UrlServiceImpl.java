package com.ganoninc.viteurlshortener.urlservice.service.impl;

import com.ganoninc.viteurlshortener.urlservice.dto.UserUrlDTO;
import com.ganoninc.viteurlshortener.urlservice.kafka.UrlCreatedProducer;
import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;
import com.ganoninc.viteurlshortener.urlservice.repository.UrlRepository;
import com.ganoninc.viteurlshortener.urlservice.service.UrlService;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UrlServiceImpl implements UrlService {

  private final UrlRepository urlRepository;
  private final UrlCreatedProducer urlCreatedProducer;

  public UrlServiceImpl(UrlRepository repository, UrlCreatedProducer producer) {
    this.urlRepository = repository;
    this.urlCreatedProducer = producer;
  }

  @WithSpan("url.create_url_mapping")
  @Override
  public UrlMapping createUrlMapping(String originalUrl, String userEmail) {
    Span currentSpan = Span.current();

    String shortId = UUID.randomUUID().toString().substring(0, 8);
    Span.current().setAttribute("url.short_id", shortId);
    UrlMapping urlMapping = new UrlMapping();
    urlMapping.setOriginalUrl(originalUrl);
    urlMapping.setShortId(shortId);
    urlMapping.setUserEmail(userEmail);

    UrlMapping savedEntity = urlRepository.save(urlMapping);
    currentSpan.addEvent(
        "url.mapping.created",
        Attributes.of(AttributeKey.longKey("url.mapping.id"), savedEntity.getId()));

    urlCreatedProducer.sendUrlCreated(savedEntity);

    return savedEntity;
  }

  @WithSpan("url.get_user_url_mappings")
  @Override
  public List<UrlMapping> getUserUrls(String userEmail) {
    return urlRepository.findAllByUserEmail(userEmail);
  }

  @WithSpan("url.get_url_mapping")
  @Override
  public Optional<UserUrlDTO> getUserUrl(@SpanAttribute("url.short_id") String shortId) {
    return urlRepository.findByShortId(shortId).map(UserUrlDTO::from);
  }
}
