package com.relay.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds {@code jwt.*} from application.yml.
 *
 * @param secret     HMAC-SHA256 signing key — must be at least 32 characters (256 bits) in production
 * @param expiration token lifetime in milliseconds
 */
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String secret, long expiration) {}