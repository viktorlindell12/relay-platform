package com.relay.message.dto;

import java.time.Instant;

/**
 * Response body for all message endpoints.
 *
 * @param id        internal message ID
 * @param senderId  authUserId of the sender
 * @param channel   name of the channel the message belongs to (e.g. "general")
 * @param content   message text
 * @param createdAt UTC timestamp when the message was persisted
 */
public record MessageResponse(Long id, Long senderId, String channel, String content, Instant createdAt) {}