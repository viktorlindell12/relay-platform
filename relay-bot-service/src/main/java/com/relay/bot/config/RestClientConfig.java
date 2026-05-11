package com.relay.bot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Configures the {@link RestClient} used to call relay-message-service.
 * The {@code X-Internal-Key} header is attached to every request so the message service
 * accepts calls from this bot.
 */
@Configuration
public class RestClientConfig {

    /**
     * @param messageServiceUrl base URL of relay-message-service (e.g. {@code http://localhost:8083})
     * @param internalApiKey    pre-shared key expected by the message service's {@code InternalApiKeyFilter}
     * @return configured {@link RestClient} with base URL and internal auth header set
     */
    @Bean
    public RestClient messageServiceRestClient(
            @Value("${services.message-url}") String messageServiceUrl,
            @Value("${internal.api-key}") String internalApiKey) {
        return RestClient.builder()
                .baseUrl(messageServiceUrl)
                .defaultHeader("X-Internal-Key", internalApiKey)
                .build();
    }
}