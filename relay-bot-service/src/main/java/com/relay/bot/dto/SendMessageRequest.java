package com.relay.bot.dto;

/**
 * Request body for {@code POST /api/messages} on relay-message-service.
 * Mirrors {@code CreateMessageRequest} without introducing a cross-service dependency.
 *
 * @param senderId authUserId of the sender (the bot's user ID)
 * @param channel  destination channel name
 * @param content  message text
 */
public record SendMessageRequest(Long senderId, String channel, String content) {}