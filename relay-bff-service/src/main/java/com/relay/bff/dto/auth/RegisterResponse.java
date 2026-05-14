package com.relay.bff.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response body for a successful registration")
public record RegisterResponse(
        @Schema(description = "Assigned user ID", example = "42") Long userId
) {}