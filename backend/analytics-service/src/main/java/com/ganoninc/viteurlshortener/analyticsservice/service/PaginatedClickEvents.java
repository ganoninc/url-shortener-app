package com.ganoninc.viteurlshortener.analyticsservice.service;

import com.ganoninc.viteurlshortener.analyticsservice.common.PaginatedSlice;

public record PaginatedClickEvents(long totalClicks, PaginatedSlice<ClickEventView, Long> events) {}
