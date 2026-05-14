package com.relay.bff.dto.user;

import java.time.Instant;

public record UserResponse(Long id, String displayName, String email, Instant createdAt) {}