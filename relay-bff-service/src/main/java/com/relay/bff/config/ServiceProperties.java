package com.relay.bff.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds {@code services.*} from application.yml — base URLs for each downstream service.
 *
 * @param authUrl    base URL for relay-auth-service
 * @param userUrl    base URL for relay-user-service
 * @param messageUrl base URL for relay-message-service
 */
@ConfigurationProperties(prefix = "services")
public record ServiceProperties(String authUrl, String userUrl, String messageUrl) {}