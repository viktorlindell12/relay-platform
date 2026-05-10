package com.relay.user.service;

import com.relay.user.dto.CreateUserProfileRequest;
import com.relay.user.dto.UpdateUserProfileRequest;
import com.relay.user.dto.UserProfileResponse;

/**
 * Business logic contract for user profile management.
 */
public interface UserProfileService {

    /**
     * Creates a new user profile linked to the given auth-service user.
     *
     * @param request profile data including the authUserId from the auth service
     * @return the created profile
     * @throws com.relay.user.exception.UserProfileAlreadyExistsException if a profile already exists for the user
     */
    UserProfileResponse create(CreateUserProfileRequest request);

    /**
     * Returns the profile for the given auth-service user ID.
     *
     * @param authUserId the user ID from the auth service
     * @return the matching profile
     * @throws com.relay.user.exception.UserNotFoundException if no profile exists for the user
     */
    UserProfileResponse getByAuthUserId(Long authUserId);

    /**
     * Updates the mutable fields of an existing profile.
     *
     * @param authUserId the auth-service user ID identifying the profile to update
     * @param request    fields to update
     * @return the updated profile
     * @throws com.relay.user.exception.UserNotFoundException if no profile exists for the user
     */
    UserProfileResponse update(Long authUserId, UpdateUserProfileRequest request);

    /**
     * Deletes the profile for the given auth-service user ID.
     *
     * @param authUserId the auth-service user ID identifying the profile to delete
     * @throws com.relay.user.exception.UserNotFoundException if no profile exists for the user
     */
    void delete(Long authUserId);
}