package com.ganoninc.viteurlshortener.analyticsservice.kafka;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ganoninc.viteurlshortener.analyticsservice.service.ClickEventService;
import com.ganoninc.viteurlshortener.analyticsservice.util.FakeKafkaUrlClickedMessagePayload;
import java.time.Instant;
import nl.altindag.log.LogCaptor;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClickEventConsumerTest {

  @InjectMocks private ClickEventConsumer clickEventConsumer;

  @Mock private ClickEventService clickEventService;

  @Test
  public void itShouldCreateClickEventWhenItReceivesAUrlClickedEventMessage() throws JSONException {
    JSONObject urlClickedMessagePayload = FakeKafkaUrlClickedMessagePayload.getFakePayload();

    clickEventConsumer.handleUrlClickedMessage(urlClickedMessagePayload.toString());

    verify(clickEventService, times(1))
        .createClickEvent(
            urlClickedMessagePayload.getString("shortId"),
            Instant.parse(urlClickedMessagePayload.getString("timestamp")),
            urlClickedMessagePayload.getString("ip"),
            urlClickedMessagePayload.getString("userAgent"));
  }

  @Test
  public void itShouldTNotSaveAClickEventEntityWhenItReceivesAnInvalidUrlClickedEventMessage()
      throws JSONException {
    JSONObject incompleteMessagePayload =
        FakeKafkaUrlClickedMessagePayload.getFakeIncompletePayload();
    clickEventConsumer.handleUrlClickedMessage(incompleteMessagePayload.toString());
    verify(clickEventService, never()).createClickEvent(any(), any(), any(), any());
  }

  @Test
  public void itShouldLogAnErrorWhenItReceivesAnInvalidUrlClickedEvent() throws JSONException {
    LogCaptor logCaptor = LogCaptor.forClass(ClickEventConsumer.class);
    JSONObject incompleteMessagePayload =
        FakeKafkaUrlClickedMessagePayload.getFakeIncompletePayload();
    clickEventConsumer.handleUrlClickedMessage(incompleteMessagePayload.toString());

    assertThat(logCaptor.getErrorLogs())
        .containsExactly("Failed to process click event message: " + incompleteMessagePayload);
  }

  @Test
  public void itShouldNotLogAnErrorWhenItReceivesAUrlClickedEvent() throws JSONException {
    LogCaptor logCaptor = LogCaptor.forClass(ClickEventConsumer.class);
    JSONObject urlClickedMessagePayload = FakeKafkaUrlClickedMessagePayload.getFakePayload();

    clickEventConsumer.handleUrlClickedMessage(urlClickedMessagePayload.toString());

    assertThat(logCaptor.getErrorLogs()).isEmpty();
  }
}
