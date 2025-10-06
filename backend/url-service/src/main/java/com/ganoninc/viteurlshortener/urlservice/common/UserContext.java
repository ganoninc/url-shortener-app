package com.ganoninc.viteurlshortener.urlservice.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class UserContext {
  public static String getUserEmail() {
    ServletRequestAttributes attrs =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest req = attrs.getRequest();
    return req.getHeader("X-User-Sub");
  }
}
