package com.relay.bff.dto.user;

public record CreateUserProfileRequest(Long authUserId, String email, String displayName) {}