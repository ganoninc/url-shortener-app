package com.ganoninc.viteurlshortener.analyticsservice.kafka;

import static com.ganoninc.viteurlshortener.analyticsservice.tracing.TelemetryAttributes.MESSAGING_ORIGIN;
import static com.ganoninc.viteurlshortener.analyticsservice.tracing.TelemetryAttributes.MESSAGING_SYSTEM;

import com.ganoninc.viteurlshortener.analyticsservice.service.ClickEventService;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.instrumentation.annotations.WithSpan;
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

  @WithSpan("analytics.create_click_event")
  @KafkaListener(
      containerFactory = "kafkaListenerContainerFactory",
      topics = "url-clicked",
      groupId = "analytics-service-group")
  public void handleUrlClickedMessage(String message) {
    Span currentSpan = Span.current();
    currentSpan.setAttribute(MESSAGING_SYSTEM, "kafka");
    currentSpan.setAttribute(MESSAGING_ORIGIN, "url-clicked");

    try {
      JSONObject messageAsJson = new JSONObject(message);
      clickEventService.createClickEvent(
          messageAsJson.getString("shortId"),
          Instant.parse(messageAsJson.getString("timestamp")),
          messageAsJson.getString("ip"),
          messageAsJson.getString("userAgent"));

      currentSpan.setAttribute("url.short_id", messageAsJson.getString("shortId"));
    } catch (JSONException e) {
      currentSpan.recordException(e);
      currentSpan.setStatus(StatusCode.ERROR, e.getMessage());
      logger.error("Failed to process click event message: {}", message, e);
    }
  }
}
