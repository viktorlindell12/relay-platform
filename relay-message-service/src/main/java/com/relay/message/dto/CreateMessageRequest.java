package com.relay.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Request body for sending a new message to a channel.
 *
 * @param senderId  authUserId of the user sending the message
 * @param channelId ID of the destination channel
 * @param content   message text, max 4000 characters
 */
public record CreateMessageRequest(
        @NotNull @Positive Long senderId,
        @NotNull @Positive Long channelId,
        @NotBlank @Size(max = 4000) String content
) {}