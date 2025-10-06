package com.ganoninc.viteurlshortener.redirectorservice.kafka;

import org.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ClickEventProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public ClickEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
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

    kafkaTemplate.send("url_clicked", payload);
  }
}
