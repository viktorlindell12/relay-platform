package com.relay.user.exception;

/**
 * Thrown when a user profile cannot be found by the given identifier.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * @param authUserId the auth-service user ID that was not found
     */
    public UserNotFoundException(Long authUserId) {
        super("User profile not found for authUserId: " + authUserId);
    }
}