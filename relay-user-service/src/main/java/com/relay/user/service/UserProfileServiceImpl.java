package com.relay.user.service;

import com.relay.user.dto.CreateUserProfileRequest;
import com.relay.user.dto.UpdateUserProfileRequest;
import com.relay.user.dto.UserProfileResponse;
import com.relay.user.entity.UserProfile;
import com.relay.user.exception.UserNotFoundException;
import com.relay.user.exception.UserProfileAlreadyExistsException;
import com.relay.user.repository.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link UserProfileService}.
 */
@Service
public class UserProfileServiceImpl implements UserProfileService {

    private static final Logger log = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    private final UserProfileRepository userProfileRepository;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserProfileResponse create(CreateUserProfileRequest request) {
        if (userProfileRepository.findByAuthUserId(request.authUserId()).isPresent()) {
            throw new UserProfileAlreadyExistsException(request.authUserId());
        }

        UserProfile profile = new UserProfile();
        profile.setAuthUserId(request.authUserId());
        profile.setDisplayName(request.displayName());

        try {
            // saveAndFlush forces immediate SQL execution so the unique constraint is checked
            // inside this try block, not deferred to transaction commit where it would escape the catch
            UserProfile saved = userProfileRepository.saveAndFlush(profile);
            log.debug("Created user profile id={} for authUserId={}", saved.getId(), saved.getAuthUserId());
            return toResponse(saved);
        } catch (DataIntegrityViolationException e) {
            // DB unique constraint guards against concurrent profile creation for the same user
            throw new UserProfileAlreadyExistsException(request.authUserId());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getByAuthUserId(Long authUserId) {
        return userProfileRepository.findByAuthUserId(authUserId)
                .map(this::toResponse)
                .orElseThrow(() -> new UserNotFoundException(authUserId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserProfileResponse update(Long authUserId, UpdateUserProfileRequest request) {
        UserProfile profile = userProfileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new UserNotFoundException(authUserId));

        profile.setDisplayName(request.displayName());
        // Dirty-checking flushes the update automatically on transaction commit
        return toResponse(profile);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void delete(Long authUserId) {
        UserProfile profile = userProfileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new UserNotFoundException(authUserId));
        userProfileRepository.delete(profile);
        log.debug("Deleted user profile for authUserId={}", authUserId);
    }

    private UserProfileResponse toResponse(UserProfile profile) {
        return new UserProfileResponse(
                profile.getId(),
                profile.getAuthUserId(),
                profile.getDisplayName(),
                profile.getCreatedAt()
        );
    }
}