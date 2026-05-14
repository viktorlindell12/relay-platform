package com.relay.bff.client;

import com.relay.bff.dto.auth.LoginRequest;
import com.relay.bff.dto.auth.LoginResponse;
import com.relay.bff.dto.auth.RegisterRequest;
import com.relay.bff.dto.auth.RegisterResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class AuthServiceClient {

    private static final Duration BLOCK_TIMEOUT = Duration.ofSeconds(10);

    private final WebClient webClient;

    public AuthServiceClient(@Qualifier("authWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public RegisterResponse register(RegisterRequest request) {
        return webClient.post()
                .uri("/api/auth/register")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(RegisterResponse.class)
                .switchIfEmpty(Mono.error(new IllegalStateException("Empty response from auth-service")))
                .block(BLOCK_TIMEOUT);
    }

    public LoginResponse login(LoginRequest request) {
        return webClient.post()
                .uri("/api/auth/login")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(LoginResponse.class)
                .switchIfEmpty(Mono.error(new IllegalStateException("Empty response from auth-service")))
                .block(BLOCK_TIMEOUT);
    }
}