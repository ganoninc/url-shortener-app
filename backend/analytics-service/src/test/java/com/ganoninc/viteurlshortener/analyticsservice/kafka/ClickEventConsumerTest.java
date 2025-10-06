package com.ganoninc.viteurlshortener.analyticsservice.kafka;

import com.ganoninc.viteurlshortener.analyticsservice.model.ClickEvent;
import com.ganoninc.viteurlshortener.analyticsservice.repository.ClickRepository;
import com.ganoninc.viteurlshortener.analyticsservice.util.FakeKafkaUrlClickedMessagePayload;
import nl.altindag.log.LogCaptor;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClickEventConsumerTest {

  @InjectMocks private ClickEventConsumer clickEventConsumer;

  @Mock private ClickRepository clickRepository;

  @Test
  public void itShouldSaveAClickEventEntityWhenItReceivesAUrlClickedEvent() throws JSONException {
    JSONObject urlClickedMessagePayload = FakeKafkaUrlClickedMessagePayload.getFakePayload();

    clickEventConsumer.handleUrlClickedMessage(urlClickedMessagePayload.toString());

    ArgumentCaptor<ClickEvent> captor = ArgumentCaptor.forClass(ClickEvent.class);
    verify(clickRepository, times(1)).save(captor.capture());

    ClickEvent savedClickEvent = captor.getValue();

    assertEquals(urlClickedMessagePayload.getString("shortId"), savedClickEvent.getShortId());
    assertEquals(urlClickedMessagePayload.getString("ip"), savedClickEvent.getIp());
    assertEquals(urlClickedMessagePayload.getString("userAgent"), savedClickEvent.getUserAgent());
    assertNotNull(savedClickEvent.getTimestamp());
  }

  @Test
  public void itShouldTNotSaveAClickEventEntityWhenItReceivesAnInvalidUrlClickedEvent()
      throws JSONException {
    JSONObject incompleteMessagePayload =
        FakeKafkaUrlClickedMessagePayload.getFakeIncompletePayload();
    clickEventConsumer.handleUrlClickedMessage(incompleteMessagePayload.toString());
    verify(clickRepository, never()).save(any(ClickEvent.class));
  }

  @Test
  public void itShouldLogAnErrorWhenItReceivesAnInvalidUrlClickedEvent() throws JSONException {
    LogCaptor logCaptor = LogCaptor.forClass(ClickEventConsumer.class);
    JSONObject incompleteMessagePayload =
        FakeKafkaUrlClickedMessagePayload.getFakeIncompletePayload();
    clickEventConsumer.handleUrlClickedMessage(incompleteMessagePayload.toString());

    assertThat(logCaptor.getErrorLogs())
        .containsExactly(
            "Failed to process click event message: " + incompleteMessagePayload.toString());
  }

  @Test
  public void itShouldNotLogAnErrorWhenItReceivesAUrlClickedEvent() throws JSONException {
    LogCaptor logCaptor = LogCaptor.forClass(ClickEventConsumer.class);
    JSONObject urlClickedMessagePayload = FakeKafkaUrlClickedMessagePayload.getFakePayload();

    clickEventConsumer.handleUrlClickedMessage(urlClickedMessagePayload.toString());

    assertThat(logCaptor.getErrorLogs()).isEmpty();
  }
}
