package com.relay.auth.controller;

import com.relay.auth.dto.LoginRequest;
import com.relay.auth.dto.LoginResponse;
import com.relay.auth.dto.RegisterRequest;
import com.relay.auth.dto.RegisterResponse;
import com.relay.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST endpoints for authentication — registration and login.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registers a new user account.
     *
     * @param request email and password
     * @return 201 Created with the new user's ID
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    /**
     * Authenticates a user and returns a signed JWT.
     *
     * @param request email and password
     * @return 200 OK with the token and userId, or 401 if credentials are invalid
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}