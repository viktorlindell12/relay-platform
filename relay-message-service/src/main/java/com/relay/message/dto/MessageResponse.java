package com.relay.message.dto;

import java.time.Instant;

/**
 * Response body for all message endpoints.
 *
 * @param id        internal message ID
 * @param senderId  authUserId of the sender
 * @param channelId ID of the channel the message belongs to
 * @param content   message text
 * @param createdAt timestamp when the message was persisted
 */
public record MessageResponse(Long id, Long senderId, Long channelId, String content, Instant createdAt) {}