package com.relay.auth.dto;

/**
 * Response body for {@code POST /api/auth/login}.
 */
public record LoginResponse(String token, Long userId) {}