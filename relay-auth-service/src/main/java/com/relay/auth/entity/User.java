package com.relay.auth.entity;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Represents a registered user in the auth service.
 * Passwords are always stored hashed — never in plaintext.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private String role = "USER";

    @PrePersist
    private void prePersist() {
        createdAt = Instant.now();
    }

    public Long getId() { return id; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public Instant getCreatedAt() { return createdAt; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }
}