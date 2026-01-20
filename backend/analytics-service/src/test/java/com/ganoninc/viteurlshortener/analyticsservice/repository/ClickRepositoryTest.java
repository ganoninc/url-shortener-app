package com.ganoninc.viteurlshortener.analyticsservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ganoninc.viteurlshortener.analyticsservice.model.ClickEvent;
import com.ganoninc.viteurlshortener.analyticsservice.util.FakeClickEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
public class ClickRepositoryTest {

  @Autowired private ClickRepository clickRepository;

  @Test
  public void itShouldSaveAndFind() {
    ClickEvent clickEvent = FakeClickEvent.getFakeClickEvent();
    clickRepository.save(clickEvent);

    ClickEvent foundClickEvent =
        clickRepository
            .findByShortIdOrderByIdDesc(clickEvent.getShortId(), Pageable.unpaged())
            .get(0);
    assertEquals(clickEvent.getShortId(), foundClickEvent.getShortId());
  }
}
