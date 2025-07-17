package com.ganoninc.viteurlshortener.redirectorservice.repository;

import com.ganoninc.viteurlshortener.redirectorservice.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlMapping, Long> {
    Optional<UrlMapping> findByShortId(String shortId);
}