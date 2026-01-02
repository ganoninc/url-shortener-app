package com.ganoninc.viteurlshortener.redirectorservice.kafka.impl;

import com.ganoninc.viteurlshortener.redirectorservice.kafka.ClickEventProducer;
import org.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ClickEventProducerImpl implements ClickEventProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public ClickEventProducerImpl(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendClickEvent(String shortId, String ip, String userAgent) {
    String payload =
        new JSONObject()
            .put("shortId", shortId)
            .put("timestamp", Instant.now().toString())
            .put("ip", ip)
            .put("userAgent", userAgent)
            .toString();

    kafkaTemplate.send("url-clicked", payload);
  }
}
