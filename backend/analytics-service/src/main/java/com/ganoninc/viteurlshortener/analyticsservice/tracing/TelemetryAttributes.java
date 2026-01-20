package com.ganoninc.viteurlshortener.analyticsservice.tracing;

import io.opentelemetry.api.common.AttributeKey;

public final class TelemetryAttributes {
  private TelemetryAttributes() {}

  //  click event
  public static final AttributeKey<Long> CLICK_EVENT_ID = AttributeKey.longKey("click_event.id");
  public static final AttributeKey<String> CLICK_EVENT_COUNTRY_CODE =
      AttributeKey.stringKey("click_event.country_code");

  //  messaging (Kafka)
  public static final AttributeKey<String> MESSAGING_SYSTEM =
      AttributeKey.stringKey("messaging.system");
  public static final AttributeKey<String> MESSAGING_ORIGIN =
      AttributeKey.stringKey("messaging.origin");

  // error
  public static final AttributeKey<String> ERROR_MESSAGE = AttributeKey.stringKey("error.message");
}
