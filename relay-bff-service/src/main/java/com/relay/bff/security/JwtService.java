package com.relay.bff.security;

import com.relay.bff.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * Validates JWTs and extracts claims.
 * Uses the same HMAC-SHA256 signing key as relay-auth-service — no network call required.
 * The signing key is derived once at construction time.
 */
@Service
public class JwtService {

    private final SecretKey signingKey;

    public JwtService(JwtProperties jwtProperties) {
        Assert.hasText(jwtProperties.secret(), "jwt.secret must not be blank");
        this.signingKey = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Returns {@code true} if the token has a valid signature and is not expired.
     *
     * @param token compact JWT string
     */
    public boolean validate(String token) {
        try {
            Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extracts the {@code userId} custom claim. Call {@link #validate(String)} first.
     *
     * @param token compact JWT string
     * @return user ID, or {@code null} if the claim is absent
     */
    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    /**
     * Extracts the subject (email) from a token. Call {@link #validate(String)} first.
     *
     * @param token compact JWT string
     * @return email stored in the {@code sub} claim
     */
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extracts the {@code role} custom claim. Call {@link #validate(String)} first.
     *
     * @param token compact JWT string
     * @return role string, e.g. {@code "USER"}, or {@code null} if the claim is absent
     */
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}