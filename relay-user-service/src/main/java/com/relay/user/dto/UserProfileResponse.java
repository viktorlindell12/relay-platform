package com.relay.user.dto;

import java.time.Instant;

/**
 * Response body for all user profile endpoints.
 */
public record UserProfileResponse(Long id, Long authUserId, String displayName, Instant createdAt) {}