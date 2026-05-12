package com.relay.bot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestClient;

import java.time.Duration;

/**
 * Configures the {@link RestClient} used to call relay-message-service and the
 * {@link TaskScheduler} used for non-blocking reply delays.
 */
@Configuration
public class RestClientConfig {

    /**
     * @param messageServiceUrl base URL of relay-message-service (e.g. {@code http://localhost:8083})
     * @param internalApiKey    pre-shared key expected by the message service's {@code InternalApiKeyFilter}
     * @return configured {@link RestClient} with base URL, internal auth header, and HTTP timeouts set
     */
    @Bean
    public RestClient messageServiceRestClient(
            @Value("${services.message-url}") String messageServiceUrl,
            @Value("${internal.api-key}") String internalApiKey) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));
        factory.setReadTimeout(Duration.ofSeconds(10));
        return RestClient.builder()
                .requestFactory(factory)
                .baseUrl(messageServiceUrl)
                .defaultHeader("X-Internal-Key", internalApiKey)
                .build();
    }

    /**
     * @return scheduler used by {@link com.relay.bot.service.BotReplyService} to fire delayed replies
     *         without blocking a thread during the wait
     */
    @Bean
    public TaskScheduler botReplyScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(2);
        scheduler.setThreadNamePrefix("bot-reply-");
        return scheduler;
    }
}