package com.relay.bff.dto;

import java.time.Instant;

/**
 * Standard error envelope returned by the BFF on all error responses.
 *
 * @param status    HTTP status code
 * @param message   human-readable error description
 * @param timestamp UTC instant when the error occurred
 */
public record ErrorResponse(int status, String message, Instant timestamp) {

    /** Convenience factory so callers don't need to pass {@code Instant.now()} manually. */
    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(status, message, Instant.now());
    }
}