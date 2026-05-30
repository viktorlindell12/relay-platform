package com.relay.bff.client;

import com.relay.bff.dto.message.MessageResponse;
import com.relay.bff.dto.message.SendMessageRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component
public class MessageServiceClient {

    private static final Duration BLOCK_TIMEOUT = Duration.ofSeconds(10);

    private final WebClient webClient;

    public MessageServiceClient(@Qualifier("messageWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public List<MessageResponse> getMessages(String channel) {
        return webClient.get()
                .uri(uri -> uri.path("/api/messages")
                        .queryParam("channel", channel)
                        .queryParam("size", 50)
                        .queryParam("sort", "createdAt,asc")
                        .build())
                .retrieve()
                .bodyToMono(MessagePage.class)
                .switchIfEmpty(Mono.error(new IllegalStateException("Empty response from message-service")))
                .map(MessagePage::content)
                .block(BLOCK_TIMEOUT);
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

    public MessageResponse togglePin(Long messageId, Long requesterId) {
        return webClient.patch()
                .uri(uri -> uri.path("/api/messages/{id}/pin")
                        .queryParam("requesterId", requesterId)
                        .build(messageId))
                .retrieve()
                .bodyToMono(MessageResponse.class)
                .switchIfEmpty(Mono.error(new IllegalStateException("Empty response from message-service")))
                .block(BLOCK_TIMEOUT);
    }

    private record MessagePage(List<MessageResponse> content) {}

    private record InternalCreateMessageRequest(Long senderId, String channel, String content) {}
}