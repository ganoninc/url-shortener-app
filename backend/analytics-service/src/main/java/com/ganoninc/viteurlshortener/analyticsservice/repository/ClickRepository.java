package com.ganoninc.viteurlshortener.analyticsservice.repository;

import com.ganoninc.viteurlshortener.analyticsservice.model.ClickEvent;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClickRepository extends JpaRepository<ClickEvent, Long> {

  long countByShortId(String shortId);

  @Query(
      "SELECT c FROM ClickEvent c WHERE c.shortId = :shortId AND c.id < :cursor ORDER BY c.id DESC")
  List<ClickEvent> findAfterCursorByShortId(
      @Param("shortId") String shortId, @Param("cursor") Long cursor, Pageable pageable);

  List<ClickEvent> findByShortIdOrderByIdAsc(String shortId, Pageable pageable);
}
