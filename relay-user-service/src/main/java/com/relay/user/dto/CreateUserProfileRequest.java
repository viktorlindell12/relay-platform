package com.relay.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Request body for {@code POST /api/users}.
 * Called by the BFF immediately after a successful registration in the auth service.
 */
public record CreateUserProfileRequest(
        @NotNull(message = "authUserId is required")
        @Positive(message = "authUserId must be a positive number")
        Long authUserId,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Display name is required")
        @Size(max = 100, message = "Display name must be at most 100 characters")
        String displayName
) {}