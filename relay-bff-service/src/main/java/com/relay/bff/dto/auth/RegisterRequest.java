package com.relay.bff.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for creating a new account")
public record RegisterRequest(
        @Schema(description = "Email address", example = "alice@example.com")
        @NotBlank @Email String email,

        @Schema(description = "Password — minimum 8 characters", example = "s3cur3P@ss")
        @NotBlank @Size(min = 8) String password
) {}