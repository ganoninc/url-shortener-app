package com.ganoninc.viteurlshortener.redirectorservice.repository;

import com.ganoninc.viteurlshortener.redirectorservice.model.UrlMapping;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<UrlMapping, Long> {
    Optional<UrlMapping> findByShortId(String shortId);
}