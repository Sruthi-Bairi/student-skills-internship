package com.ssims.dto;

public record CsrfResponse(String token, String headerName, String cookieName) {
}
