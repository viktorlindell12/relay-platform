package com.relay.bff.client;

import com.relay.bff.dto.message.MessageResponse;
import com.relay.bff.dto.message.SendMessageRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class MessageServiceClient {

    private static final Duration BLOCK_TIMEOUT = Duration.ofSeconds(10);

    private final WebClient webClient;

    public MessageServiceClient(@Qualifier("messageWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public MessageResponse send(Long senderId, SendMessageRequest request) {
        var internalRequest = new InternalCreateMessageRequest(senderId, request.channel(), request.content());
        return webClient.post()
                .uri("/api/messages")
                .bodyValue(internalRequest)
                .retrieve()
                .bodyToMono(MessageResponse.class)
                .switchIfEmpty(Mono.error(new IllegalStateException("Empty response from message-service")))
                .block(BLOCK_TIMEOUT);
    }

    private record InternalCreateMessageRequest(Long senderId, String channel, String content) {}
}