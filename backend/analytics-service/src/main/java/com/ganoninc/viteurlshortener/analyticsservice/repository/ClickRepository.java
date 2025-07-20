package com.ganoninc.viteurlshortener.analyticsservice.repository;

import com.ganoninc.viteurlshortener.analyticsservice.model.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClickRepository extends JpaRepository<ClickEvent, Long> {
    List<ClickEvent> findByShortId(String shortId);
}
