package com.relay.bff.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response body for a successful login")
public record LoginResponse(
        @Schema(description = "Signed JWT — include as 'Authorization: Bearer <token>' on subsequent requests") String token,
        @Schema(description = "Authenticated user ID", example = "42") Long userId
) {}