package com.relay.bff.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for sending a message to a channel")
public record SendMessageRequest(
        @Schema(description = "Target channel name", example = "general")
        @NotBlank @Size(max = 100) String channel,

        @Schema(description = "Message text — max 4000 characters", example = "Hello, world!")
        @NotBlank @Size(max = 4000) String content
) {}