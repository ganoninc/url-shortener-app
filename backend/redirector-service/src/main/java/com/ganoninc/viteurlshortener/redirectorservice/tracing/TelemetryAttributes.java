package com.ganoninc.viteurlshortener.redirectorservice.tracing;

import io.opentelemetry.api.common.AttributeKey;

public final class TelemetryAttributes {
  private TelemetryAttributes() {}

  //  messaging (Kafka)
  public static final AttributeKey<String> MESSAGING_SYSTEM =
      AttributeKey.stringKey("messaging.system");
  public static final AttributeKey<String> MESSAGING_DESTINATION =
      AttributeKey.stringKey("messaging.destination");

  // error
  public static final AttributeKey<String> ERROR_MESSAGE = AttributeKey.stringKey("error.message");
}
