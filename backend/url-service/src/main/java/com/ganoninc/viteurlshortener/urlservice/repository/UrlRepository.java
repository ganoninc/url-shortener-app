package com.ganoninc.viteurlshortener.urlservice.repository;

import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlMapping, Long> {
    List<UrlMapping> findAllByUserEmail(String email);
    Optional<UrlMapping> findByShortId(String shortId);
}
