package com.relay.bff.client;

import com.relay.bff.dto.message.MessageResponse;
import com.relay.bff.dto.message.SendMessageRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class MessageServiceClient {

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
                .block();
    }

    private record InternalCreateMessageRequest(Long senderId, String channel, String content) {}
}