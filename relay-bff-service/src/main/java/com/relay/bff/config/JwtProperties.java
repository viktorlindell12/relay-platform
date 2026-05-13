package com.relay.bff.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds {@code jwt.*} from application.yml.
 * The BFF only validates tokens — no expiration config needed since that is encoded in the token itself.
 *
 * @param secret HMAC-SHA256 signing key — must match the key used by relay-auth-service
 */
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String secret) {}