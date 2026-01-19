package com.ganoninc.viteurlshortener.redirectorservice.kafka.impl;

import com.ganoninc.viteurlshortener.redirectorservice.kafka.ClickEventProducer;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import java.time.Instant;
import org.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ClickEventProducerImpl implements ClickEventProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public ClickEventProducerImpl(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendClickEvent(String shortId, String ip, String userAgent) {
    Span currentSpan = Span.current();
    String payload =
        new JSONObject()
            .put("shortId", shortId)
            .put("timestamp", Instant.now().toString())
            .put("ip", ip)
            .put("userAgent", userAgent)
            .toString();

    kafkaTemplate
        .send("url-clicked", payload)
        .whenComplete(
            (result, error) -> {
              if (error != null) {
                currentSpan.recordException(error);
                currentSpan.addEvent(
                    "kafka.publish.failure",
                    Attributes.of(
                        AttributeKey.stringKey("messaging.system"), "kafka",
                        AttributeKey.stringKey("messaging.destination"), "url-clicked",
                        AttributeKey.stringKey("error.message"), error.getMessage()));
              }
            });

    currentSpan.addEvent(
        "kafka.produce",
        Attributes.of(
            AttributeKey.stringKey("messaging.system"), "kafka",
            AttributeKey.stringKey("messaging.destination"), "url-clicked"));
  }
}
