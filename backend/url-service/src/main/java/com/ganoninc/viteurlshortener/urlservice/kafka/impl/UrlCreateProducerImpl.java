package com.ganoninc.viteurlshortener.urlservice.kafka.impl;

import com.ganoninc.viteurlshortener.urlservice.kafka.UrlCreatedProducer;
import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
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
    Span currentSpan = Span.current();
    String payload =
        new JSONObject()
            .put("shortId", urlMapping.getShortId())
            .put("originalUrl", urlMapping.getOriginalUrl())
            .put("userEmail", urlMapping.getUserEmail())
            .toString();

    kafkaTemplate
        .send("url-created", payload)
        .whenComplete(
            (result, error) -> {
              if (error != null) {
                currentSpan.recordException(error);
                currentSpan.addEvent(
                    "kafka.publish.failure",
                    Attributes.of(
                        AttributeKey.stringKey("messaging.system"), "kafka",
                        AttributeKey.stringKey("messaging.destination"), "url-created",
                        AttributeKey.stringKey("error.message"), error.getMessage()));
              }
            });

    currentSpan.addEvent(
        "kafka.produce",
        Attributes.of(
            AttributeKey.stringKey("messaging.system"), "kafka",
            AttributeKey.stringKey("messaging.destination"), "url-created"));
  }
}
