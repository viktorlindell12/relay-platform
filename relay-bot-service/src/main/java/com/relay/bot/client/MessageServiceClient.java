package com.relay.bot.client;

import com.relay.bot.dto.SendMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * HTTP client for relay-message-service.
 * Failures are logged and swallowed so a transient message-service outage
 * does not crash the bot.
 */
@Component
public class MessageServiceClient {

    private static final Logger log = LoggerFactory.getLogger(MessageServiceClient.class);

    private final RestClient restClient;

    public MessageServiceClient(RestClient messageServiceRestClient) {
        this.restClient = messageServiceRestClient;
    }

    /**
     * Posts a new message to the given channel on behalf of the bot.
     *
     * @param senderId authUserId of the bot
     * @param channel  destination channel
     * @param content  message text to post
     */
    public void postMessage(Long senderId, String channel, String content) {
        restClient.post()
                .uri("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new SendMessageRequest(senderId, channel, content))
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), (req, resp) ->
                        log.warn("Failed to post bot reply to channel={}: HTTP {}", channel, resp.getStatusCode()))
                .toBodilessEntity();
    }
}