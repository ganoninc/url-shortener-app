package com.ganoninc.viteurlshortener.urlservice.repository;

import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UrlRepository extends JpaRepository<UrlMapping, Long> {
    List<UrlMapping> findAllByUserEmail(String email);
}
