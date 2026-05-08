package com.relay.auth.exception;

import java.time.Instant;

/**
 * Uniform error payload returned by {@link GlobalExceptionHandler} for all error responses.
 */
public record ErrorResponse(int status, String message, String timestamp) {

    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(status, message, Instant.now().toString());
    }
}
