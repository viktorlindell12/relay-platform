package com.relay.user.repository;

import com.relay.user.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link UserProfile} entities.
 */
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    /**
     * Finds a profile by its corresponding auth-service user ID.
     *
     * @param authUserId the user ID from the auth service
     * @return the matching profile, or empty if not found
     */
    Optional<UserProfile> findByAuthUserId(Long authUserId);
}