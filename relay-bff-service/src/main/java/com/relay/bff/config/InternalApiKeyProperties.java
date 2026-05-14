package com.relay.bff.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

/**
 * Binds {@code internal.api-key} from application.yml.
 * The BFF sends this as the {@code X-Internal-Key} header when calling relay-user-service
 * and relay-message-service.
 *
 * @param apiKey pre-shared key that must match the key configured on the downstream services
 */
@ConfigurationProperties(prefix = "internal")
public record InternalApiKeyProperties(String apiKey) {
    public InternalApiKeyProperties {
        Assert.hasText(apiKey, "internal.api-key must not be blank");
    }
}