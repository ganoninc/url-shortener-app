package com.ganoninc.viteurlshortener.analyticsservice.util;

import com.ganoninc.viteurlshortener.analyticsservice.model.ClickEvent;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class FakeClickEvent {
  public static ClickEvent getFakeClickEvent(long id) {
    ClickEvent clickEvent = getFakeClickEvent();
    clickEvent.setId(id);
    return clickEvent;
  }

  public static ClickEvent getFakeClickEvent() {
    ClickEvent clickEvent = new ClickEvent();

    clickEvent.setTimestamp(new Date().toInstant());
    clickEvent.setShortId("abcd");
    clickEvent.setUserAgent("Mozilla");
    clickEvent.setIp("127.0.0.1");

    return clickEvent;
  }

  public static List<ClickEvent> getListOfFakeClickEvent() {
    List<ClickEvent> list = new LinkedList<>();
    list.add(getFakeClickEvent(1L));
    list.add(getFakeClickEvent(2L));
    list.add(getFakeClickEvent(3L));
    list.add(getFakeClickEvent(4L));
    list.add(getFakeClickEvent(5L));
    list.add(getFakeClickEvent(6L));
    list.add(getFakeClickEvent(7L));
    list.add(getFakeClickEvent(8L));
    list.add(getFakeClickEvent(9L));
    list.add(getFakeClickEvent(10L));

    return list;
  }
}
