package com.relay.user.exception;

/**
 * Thrown when a profile creation is attempted for an auth-service user that already has one.
 */
public class UserProfileAlreadyExistsException extends RuntimeException {

    /**
     * @param authUserId the auth-service user ID that already has a profile
     */
    public UserProfileAlreadyExistsException(Long authUserId) {
        super("User profile already exists for authUserId: " + authUserId);
    }
}