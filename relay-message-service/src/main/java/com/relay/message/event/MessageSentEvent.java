package com.relay.message.event;

import java.time.Instant;

/**
 * Payload of the {@code message.published} RabbitMQ event.
 * Serialized to JSON by Jackson and consumed by downstream services (e.g. relay-bot-service).
 *
 * @param messageId  internal ID of the message that was sent
 * @param senderId   authUserId of the sender
 * @param channelId  ID of the channel the message was posted to
 * @param content    message text
 * @param createdAt  timestamp when the message was persisted
 */
public record MessageSentEvent(Long messageId, Long senderId, Long channelId, String content, Instant createdAt) {}