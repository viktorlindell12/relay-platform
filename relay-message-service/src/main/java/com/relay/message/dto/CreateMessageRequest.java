package com.relay.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Request body for sending a new message to a channel.
 *
 * @param senderId authUserId of the user sending the message
 * @param channel  destination channel name (e.g. "general")
 * @param content  message text, max 4000 characters
 */
public record CreateMessageRequest(
        @NotNull @Positive Long senderId,
        @NotBlank @Size(max = 100) String channel,
        @NotBlank @Size(max = 4000) String content
) {}