package com.relay.auth.dto;

/**
 * Response body for a successful registration containing the new user's ID.
 */
public record RegisterResponse(Long userId) {}