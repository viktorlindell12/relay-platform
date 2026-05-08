package com.relay.auth.exception;

/**
 * Thrown when a registration attempt uses an email address that is already registered.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("Email already registered: " + email);
    }
}