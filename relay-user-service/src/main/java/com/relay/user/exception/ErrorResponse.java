package com.relay.user.exception;

import java.time.Instant;

/**
 * Structured error payload returned by {@link GlobalExceptionHandler}.
 */
public record ErrorResponse(int status, String message, String timestamp) {

    /**
     * Creates an {@link ErrorResponse} with the current timestamp.
     *
     * @param status  HTTP status code
     * @param message human-readable description of the error
     */
    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(status, message, Instant.now().toString());
    }
}