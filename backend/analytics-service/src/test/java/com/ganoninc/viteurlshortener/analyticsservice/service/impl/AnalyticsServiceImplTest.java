package com.ganoninc.viteurlshortener.analyticsservice.service.impl;

import com.ganoninc.viteurlshortener.analyticsservice.model.ClickEvent;
import com.ganoninc.viteurlshortener.analyticsservice.repository.ClickRepository;
import com.ganoninc.viteurlshortener.analyticsservice.util.FakeClickEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceImplTest {

  private final String wrongShortId = "wrongShortId";

  @InjectMocks AnalyticsServiceImpl analyticsServiceImpl;

  @Mock private ClickRepository clickRepository;

  @Test
  public void itShouldGetClickCountOfShortId() {
    List<ClickEvent> listOfEvents = FakeClickEvent.getListOfFakeClickEvent();
    String shortId = listOfEvents.get(0).getShortId();
    when(clickRepository.findByShortId(shortId)).thenReturn(listOfEvents);

    long clickCount = analyticsServiceImpl.getClickCount(shortId);

    assertEquals(listOfEvents.size(), clickCount);
    verify(clickRepository, times(1)).findByShortId(shortId);
  }

  @Test
  public void itShouldReturnAListOfClickEventOfShortId() {
    List<ClickEvent> listOfEvents = FakeClickEvent.getListOfFakeClickEvent();
    String shortId = listOfEvents.get(0).getShortId();
    when(clickRepository.findByShortId(shortId)).thenReturn(listOfEvents);

    List<ClickEvent> clickEvents = analyticsServiceImpl.getAllEvents(shortId);

    assertEquals(listOfEvents.size(), clickEvents.size());
    verify(clickRepository, times(1)).findByShortId(shortId);
  }

  @Test
  public void itShouldReturnAClickCountOfZeroWhenItsCalledWithAWrongShortId() {
    List<ClickEvent> listOfEvents = new ArrayList<>();
    when(clickRepository.findByShortId(wrongShortId)).thenReturn(listOfEvents);

    long clickCount = analyticsServiceImpl.getClickCount(wrongShortId);

    verify(clickRepository, times(1)).findByShortId(wrongShortId);
    assertEquals(0, clickCount);
  }

  @Test
  public void itShouldReturnAnEmptyListWhenItsCalledWithAWrongShortId() {
    List<ClickEvent> listOfEvents = new ArrayList<>();
    when(clickRepository.findByShortId(wrongShortId)).thenReturn(listOfEvents);

    List<ClickEvent> clickEvents = analyticsServiceImpl.getAllEvents(wrongShortId);

    verify(clickRepository, times(1)).findByShortId(wrongShortId);
    assertEquals(0, clickEvents.size());
  }
}
