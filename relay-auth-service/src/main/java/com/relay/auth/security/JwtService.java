package com.relay.auth.security;

import com.relay.auth.config.JwtProperties;
import com.relay.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Generates, validates, and extracts claims from JWTs signed with HMAC-SHA256.
 * The signing key and token lifetime are configured via {@link JwtProperties}.
 */
@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * Generates a signed JWT for the given user.
     *
     * @param user the authenticated user
     * @return compact JWT string
     */
    public String generate(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.expiration());

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey())
                .compact();
    }

    /**
     * Returns {@code true} if the token has a valid signature and is not expired.
     *
     * @param token compact JWT string
     */
    public boolean validate(String token) {
        try {
            Jwts.parser().verifyWith(signingKey()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extracts the subject (email) from a token. Call {@link #validate(String)} first.
     *
     * @param token compact JWT string
     * @return email address stored in the {@code sub} claim
     */
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extracts the {@code userId} custom claim. Call {@link #validate(String)} first.
     *
     * @param token compact JWT string
     * @return user ID
     */
    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    /**
     * Extracts the {@code role} custom claim. Call {@link #validate(String)} first.
     *
     * @param token compact JWT string
     * @return role string, e.g. {@code "USER"}
     */
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey signingKey() {
        byte[] keyBytes = jwtProperties.secret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}