package com.ganoninc.viteurlshortener.redirectorservice.kafka;

public interface ClickEventProducer {
  void sendClickEvent(String shortId, String ip, String userAgent);
}
