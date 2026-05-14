package com.relay.bff.client;

import com.relay.bff.dto.auth.LoginRequest;
import com.relay.bff.dto.auth.LoginResponse;
import com.relay.bff.dto.auth.RegisterRequest;
import com.relay.bff.dto.auth.RegisterResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AuthServiceClient {

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
                .block();
    }

    public LoginResponse login(LoginRequest request) {
        return webClient.post()
                .uri("/api/auth/login")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(LoginResponse.class)
                .block();
    }
}