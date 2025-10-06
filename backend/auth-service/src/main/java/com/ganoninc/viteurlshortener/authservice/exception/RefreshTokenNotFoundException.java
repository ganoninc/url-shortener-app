package com.ganoninc.viteurlshortener.authservice.exception;

public class RefreshTokenNotFoundException extends RuntimeException {
  public RefreshTokenNotFoundException(String message) {
    super(message);
  }
}
