package com.relay.bff.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Standard error envelope returned on all error responses")
public record ErrorResponse(
        @Schema(description = "HTTP status code", example = "400") int status,
        @Schema(description = "Human-readable error description", example = "email: must not be blank") String message,
        @Schema(description = "UTC timestamp when the error occurred") Instant timestamp
) {
    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(status, message, Instant.now());
    }
}