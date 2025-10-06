package com.ganoninc.viteurlshortener.authservice.dto;

public record TokenPairDto(String jwtAccessToken, String refreshToken) {}
