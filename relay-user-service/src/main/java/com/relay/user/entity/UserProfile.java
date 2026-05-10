package com.relay.user.entity;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Stores public profile information for a registered user.
 * {@code authUserId} links to the corresponding user in the auth service — no FK constraint
 * since the services own separate databases.
 */
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auth_user_id", nullable = false, unique = true)
    private Long authUserId;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    private void prePersist() {
        createdAt = Instant.now();
    }

    /** @return internal profile ID */
    public Long getId() { return id; }

    /** @return ID of the corresponding user in the auth service */
    public Long getAuthUserId() { return authUserId; }

    /** @param authUserId ID of the corresponding user in the auth service */
    public void setAuthUserId(Long authUserId) { this.authUserId = authUserId; }

    /** @return publicly visible display name */
    public String getDisplayName() { return displayName; }

    /** @param displayName publicly visible display name */
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    /** @return timestamp when this profile was created */
    public Instant getCreatedAt() { return createdAt; }
}