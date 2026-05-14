package com.relay.bff.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "User profile as seen by the authenticated user")
public record UserResponse(
        @Schema(description = "User ID", example = "42") Long id,
        @Schema(description = "Display name", example = "Alice") String displayName,
        @Schema(description = "Email address", example = "alice@example.com") String email,
        @Schema(description = "UTC timestamp when the account was created") Instant createdAt
) {}