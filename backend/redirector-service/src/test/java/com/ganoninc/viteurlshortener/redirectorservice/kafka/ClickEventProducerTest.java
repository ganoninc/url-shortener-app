package com.ganoninc.viteurlshortener.redirectorservice.kafka;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ClickEventProducerTest {

    @Autowired
    private ClickEventProducer clickEventProducer;

    @MockitoBean
    private KafkaTemplate<String, String> kafkaTemplate;

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
