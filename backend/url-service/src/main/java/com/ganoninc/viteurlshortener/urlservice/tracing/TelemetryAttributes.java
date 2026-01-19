package com.ganoninc.viteurlshortener.urlservice.tracing;

import io.opentelemetry.api.common.AttributeKey;

public final class TelemetryAttributes {
  private TelemetryAttributes() {}

  //  url mapping
  public static final AttributeKey<Long> URL_MAPPING_ID = AttributeKey.longKey("url.mapping.id");

  //  messaging (Kafka)
  public static final AttributeKey<String> MESSAGING_SYSTEM =
      AttributeKey.stringKey("messaging.system");
  public static final AttributeKey<String> MESSAGING_DESTINATION =
      AttributeKey.stringKey("messaging.destination");

  // error
  public static final AttributeKey<String> ERROR_MESSAGE = AttributeKey.stringKey("error.message");
}
