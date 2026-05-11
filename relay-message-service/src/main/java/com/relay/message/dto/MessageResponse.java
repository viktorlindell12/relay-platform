package com.relay.message.dto;

import java.time.Instant;

/**
 * Response body for all message endpoints.
 *
 * @param id                 internal message ID
 * @param senderId           authUserId of the sender
 * @param senderDisplayName  display name resolved from User Service, or "Unknown User" if unreachable
 * @param channel            name of the channel the message belongs to (e.g. "general")
 * @param content            message text
 * @param createdAt          UTC timestamp when the message was persisted
 */
public record MessageResponse(Long id, Long senderId, String senderDisplayName, String channel, String content, Instant createdAt) {}