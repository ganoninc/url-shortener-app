package com.ganoninc.viteurlshortener.analyticsservice.service.impl;

import com.ganoninc.viteurlshortener.analyticsservice.model.ClickEvent;
import com.ganoninc.viteurlshortener.analyticsservice.repository.ClickRepository;
import com.ganoninc.viteurlshortener.analyticsservice.service.AnalyticsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final ClickRepository clickRepository;

    public AnalyticsServiceImpl(ClickRepository clickRepository) {
        this.clickRepository = clickRepository;
    }

    @Override
    public long getClickCount(String shortId) {
        return clickRepository.findByShortId(shortId).size();
    }

    @Override
    public List<ClickEvent> getAllEvents(String shortId) {
        return clickRepository.findByShortId(shortId);
    }
}
