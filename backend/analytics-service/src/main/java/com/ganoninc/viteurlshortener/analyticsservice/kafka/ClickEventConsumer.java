package com.ganoninc.viteurlshortener.analyticsservice.kafka;

import com.ganoninc.viteurlshortener.analyticsservice.service.ClickEventService;
import java.time.Instant;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ClickEventConsumer {
  private final ClickEventService clickEventService;
  private static final Logger logger = LoggerFactory.getLogger(ClickEventConsumer.class);

  public ClickEventConsumer(ClickEventService clickEventService) {
    this.clickEventService = clickEventService;
  }

  @KafkaListener(
      containerFactory = "kafkaListenerContainerFactory",
      topics = "url-clicked",
      groupId = "analytics-service-group")
  public void handleUrlClickedMessage(String message) {
    try {
      JSONObject messageAsJson = new JSONObject(message);
      clickEventService.createClickEvent(
          messageAsJson.getString("shortId"),
          Instant.parse(messageAsJson.getString("timestamp")),
          messageAsJson.getString("ip"),
          messageAsJson.getString("userAgent"));
    } catch (JSONException e) {
      logger.error("Failed to process click event message: {}", message, e);
    }
  }
}
