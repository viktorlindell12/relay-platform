package com.relay.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request body for {@code PUT /api/users/{id}}.
 */
public record UpdateUserProfileRequest(
        @NotBlank(message = "Display name is required")
        @Size(max = 100, message = "Display name must be at most 100 characters")
        String displayName
) {}