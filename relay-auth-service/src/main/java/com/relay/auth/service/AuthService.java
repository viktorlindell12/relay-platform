package com.relay.auth.service;

import com.relay.auth.dto.RegisterRequest;
import com.relay.auth.dto.RegisterResponse;
import com.relay.auth.entity.User;
import com.relay.auth.exception.EmailAlreadyExistsException;
import com.relay.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic for authentication — registration and login.
 */
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user, hashing the password before persistence.
     *
     * @throws EmailAlreadyExistsException if the email is already taken
     */
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password()));

        try {
            User saved = userRepository.save(user);
            log.info("Registered new user with id={}", saved.getId());
            return new RegisterResponse(saved.getId());
        } catch (DataIntegrityViolationException e) {
            // DB unique constraint is the real guard against concurrent registrations with the same email
            throw new EmailAlreadyExistsException(email);
        }
    }
}