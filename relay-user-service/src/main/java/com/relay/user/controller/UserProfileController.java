package com.relay.user.controller;

import com.relay.user.dto.CreateUserProfileRequest;
import com.relay.user.dto.UpdateUserProfileRequest;
import com.relay.user.dto.UserProfileResponse;
import com.relay.user.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Internal REST endpoints for user profile management.
 * All routes require the {@code X-Internal-Key} header — they are not exposed publicly.
 */
@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    /**
     * Creates a new user profile. Called by the BFF after a successful registration.
     *
     * @param request authUserId and displayName
     * @return 201 Created with the new profile
     */
    @PostMapping
    public ResponseEntity<UserProfileResponse> create(@Valid @RequestBody CreateUserProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userProfileService.create(request));
    }

    /**
     * Returns the profile for the given auth-service user ID.
     *
     * @param id the auth-service user ID
     * @return 200 OK with the profile, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(userProfileService.getByAuthUserId(id));
    }

    /**
     * Updates the display name of an existing profile.
     *
     * @param id      the auth-service user ID
     * @param request fields to update
     * @return 200 OK with the updated profile, or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserProfileResponse> update(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateUserProfileRequest request) {
        return ResponseEntity.ok(userProfileService.update(id, request));
    }

    /**
     * Deletes the profile for the given auth-service user ID.
     *
     * @param id the auth-service user ID
     * @return 204 No Content, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userProfileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}