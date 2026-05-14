package com.relay.bff.client;

import com.relay.bff.dto.user.UserResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class UserServiceClient {

    private static final Duration BLOCK_TIMEOUT = Duration.ofSeconds(10);

    private final WebClient webClient;

    public UserServiceClient(@Qualifier("userWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public UserResponse getUser(Long id) {
        return webClient.get()
                .uri("/api/users/{id}", id)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .switchIfEmpty(Mono.error(new IllegalStateException("Empty response from user-service")))
                .block(BLOCK_TIMEOUT);
    }
}