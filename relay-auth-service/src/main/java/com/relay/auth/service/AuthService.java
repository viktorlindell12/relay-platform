package com.relay.auth.service;

import com.relay.auth.dto.LoginRequest;
import com.relay.auth.dto.LoginResponse;
import com.relay.auth.dto.RegisterRequest;
import com.relay.auth.dto.RegisterResponse;
import com.relay.auth.entity.User;
import com.relay.auth.exception.EmailAlreadyExistsException;
import com.relay.auth.repository.UserRepository;
import com.relay.auth.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Business logic for authentication — registration and login.
 */
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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

    /**
     * Authenticates a user and returns a signed JWT.
     * Both "not found" and "wrong password" return the same 401 to prevent user enumeration.
     *
     * @throws ResponseStatusException 401 if credentials are invalid
     */
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(request.password(), u.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        String token = jwtService.generate(user);
        log.info("User logged in with id={}", user.getId());
        return new LoginResponse(token, user.getId());
    }
}