package com.relay.bff.dto.auth;

public record LoginResponse(String token, Long userId) {}