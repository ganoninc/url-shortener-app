package com.ganoninc.viteurlshortener.analyticsservice.repository;

import com.ganoninc.viteurlshortener.analyticsservice.model.ClickEvent;
import com.ganoninc.viteurlshortener.analyticsservice.util.FakeClickEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ClickRepositoryTest {

  @Autowired private ClickRepository clickRepository;

  @Test
  public void itShouldSaveAndFind() {
    ClickEvent clickEvent = FakeClickEvent.getFakeClickEvent();
    clickRepository.save(clickEvent);

    ClickEvent foundClickEvent = clickRepository.findByShortId(clickEvent.getShortId()).get(0);
    assertEquals(clickEvent.getShortId(), foundClickEvent.getShortId());
  }
}
