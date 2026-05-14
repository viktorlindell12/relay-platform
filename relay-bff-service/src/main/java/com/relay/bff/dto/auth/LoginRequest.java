package com.relay.bff.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request body for authentication")
public record LoginRequest(
        @Schema(description = "Registered email address", example = "alice@example.com")
        @NotBlank @Email String email,

        @Schema(description = "Account password", example = "s3cur3P@ss")
        @NotBlank String password
) {}