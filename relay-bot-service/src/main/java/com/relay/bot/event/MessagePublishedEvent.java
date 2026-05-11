package com.relay.bot.event;

import java.time.Instant;

/**
 * Inbound payload of the {@code message.published} RabbitMQ event.
 * Must match the fields published by relay-message-service's {@code MessageSentEvent}.
 *
 * @param messageId internal ID of the message that was sent
 * @param senderId  authUserId of the sender
 * @param channel   name of the channel the message was posted to (e.g. "general")
 * @param content   message text
 * @param timestamp UTC timestamp when the message was persisted
 */
public record MessagePublishedEvent(Long messageId, Long senderId, String channel, String content, Instant timestamp) {}