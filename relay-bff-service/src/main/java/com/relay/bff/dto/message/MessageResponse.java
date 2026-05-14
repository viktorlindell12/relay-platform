package com.relay.bff.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "A message as returned to the client")
public record MessageResponse(
        @Schema(description = "Message ID", example = "1") Long id,
        @Schema(description = "Display name of the sender", example = "Alice") String senderDisplayName,
        @Schema(description = "Channel the message belongs to", example = "general") String channel,
        @Schema(description = "Message text", example = "Hello, world!") String content,
        @Schema(description = "UTC timestamp when the message was sent") Instant createdAt
) {}