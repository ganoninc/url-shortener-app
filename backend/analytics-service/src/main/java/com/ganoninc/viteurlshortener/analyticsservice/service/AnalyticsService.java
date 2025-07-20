package com.ganoninc.viteurlshortener.analyticsservice.service;


import com.ganoninc.viteurlshortener.analyticsservice.model.ClickEvent;

import java.util.List;

public interface AnalyticsService {

    long getClickCount(String shortId);

    List<ClickEvent> getAllEvents(String shortId);
}
