package com.ganoninc.viteurlshortener.urlservice.kafka.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UrlCreateProducerImplTest {
    @Test
    void itShouldSendCorrectKafkaMessage() throws JsonProcessingException {
        KafkaTemplate<String, String> kafkaTemplate = mock(KafkaTemplate.class);
        UrlCreateProducerImpl urlCreateProducerImpl = new UrlCreateProducerImpl(kafkaTemplate);

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl("https://a-quite-long-url.com/that-could-be-shortened");
        urlMapping.setShortId("testId01");
        urlMapping.setUserEmail("test@test.com");

        urlCreateProducerImpl.sendUrlCreated(urlMapping);

        ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaTemplate).send(eq("url-created"), payloadCaptor.capture());

        String payload = payloadCaptor.getValue();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(payload);

        assertEquals("testId01", json.get("shortId").asText());
        assertEquals("https://a-quite-long-url.com/that-could-be-shortened", json.get("originalUrl").asText());
        assertEquals("test@test.com", json.get("userEmail").asText());
    }
}
