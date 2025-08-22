package com.ganoninc.viteurlshortener.analyticsservice.service.impl;

import com.ganoninc.viteurlshortener.analyticsservice.model.ClickEvent;
import com.ganoninc.viteurlshortener.analyticsservice.repository.ClickRepository;
import com.ganoninc.viteurlshortener.analyticsservice.service.AnalyticsService;
import com.ganoninc.viteurlshortener.analyticsservice.util.FakeClickEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = AnalyticsServiceImpl.class)
public class AnalyticsServiceImplTest {

    @Autowired
    AnalyticsService analyticsService;

    @MockitoBean
    private ClickRepository clickRepository;

    @Test
    public void itShouldGetClickCountOfShortId(){
        List<ClickEvent> listOfEvents = FakeClickEvent.getListOfFakeClickEvent();
        String shortId = listOfEvents.get(0).getShortId();
        when(clickRepository.findByShortId(shortId)).thenReturn(listOfEvents);

        long clickCount = analyticsService.getClickCount(shortId);
        assertEquals(listOfEvents.size(), clickCount);
        verify(clickRepository, times(1)).findByShortId(shortId);
    }

    @Test
    public void itShouldReturnAListOfClickEventOfShortId(){
        List<ClickEvent> listOfEvents = FakeClickEvent.getListOfFakeClickEvent();
        String shortId = listOfEvents.get(0).getShortId();
        when(clickRepository.findByShortId(shortId)).thenReturn(listOfEvents);

        List<ClickEvent> clickEvents = analyticsService.getAllEvents(shortId);
        assertEquals(listOfEvents.size(), clickEvents.size());
        verify(clickRepository, times(1)).findByShortId(shortId);
    }

    @Test
    public void itShouldReturnAClickCountOfZeroWhenItsCalledWithAWrongShortId(){
        String wrongShortId = "wrongShortId";
        List<ClickEvent> listOfEvents = new ArrayList<>();
        when(clickRepository.findByShortId(wrongShortId)).thenReturn(listOfEvents);

        long clickCount = analyticsService.getClickCount(wrongShortId);
        verify(clickRepository, times(1)).findByShortId(wrongShortId);
        assertEquals(0, clickCount);
    }

    @Test
    public void itShouldReturnAnEmptyListWhenItsCalledWithAWrongShortId(){
        String wrongShortId = "wrongShortId";
        List<ClickEvent> listOfEvents = new ArrayList<>();
        when(clickRepository.findByShortId(wrongShortId)).thenReturn(listOfEvents);

        List<ClickEvent> clickEvents = analyticsService.getAllEvents(wrongShortId);
        verify(clickRepository, times(1)).findByShortId(wrongShortId);
        assertEquals(0, clickEvents.size());
    }
}
