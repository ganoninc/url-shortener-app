package com.ganoninc.viteurlshortener.analyticsservice.kafka;

import com.ganoninc.viteurlshortener.analyticsservice.model.ClickEvent;
import com.ganoninc.viteurlshortener.analyticsservice.repository.ClickRepository;
import org.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ClickEventConsumer {
    private final ClickRepository clickRepository;

    public ClickEventConsumer(ClickRepository clickRepository) {
        this.clickRepository = clickRepository;
    }

    @KafkaListener(topics = "url_clicked", groupId = "analytics-group")
    public void handleUrlClickedMessage(String message){
        JSONObject messageAsJson = new JSONObject(message);

        ClickEvent clickEvent = new ClickEvent();
        clickEvent.setShortId(messageAsJson.getString("shortId"));
        clickEvent.setTimestamp(Instant.parse(messageAsJson.getString("timestamp")));
        clickEvent.setIp(messageAsJson.getString("ip"));
        clickEvent.setUserAgent(messageAsJson.getString("userAgent"));

        clickRepository.save(clickEvent);
    }
}
