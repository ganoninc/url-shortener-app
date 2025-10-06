package com.ganoninc.viteurlshortener.redirectorservice.kafka;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ClickEventProducerTest {

  @InjectMocks private ClickEventProducer clickEventProducer;

  @Mock private KafkaTemplate<String, String> kafkaTemplate;

  @Test
  public void itShouldSendUrlClickedToKafka() throws Exception {
    String shortId = "abc123";
    String ip = "192.168.1.1";
    String userAgent = "Mozilla/5.0";

    clickEventProducer.sendClickEvent(shortId, ip, userAgent);

    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
    verify(kafkaTemplate, times(1)).send(topicCaptor.capture(), payloadCaptor.capture());

    assertEquals("url_clicked", topicCaptor.getValue());

    String payload = payloadCaptor.getValue();
    assertTrue(payload.contains(shortId));
    assertTrue(payload.contains(ip));
    assertTrue(payload.contains(userAgent));
    assertTrue(payload.contains("timestamp"));
  }
}
