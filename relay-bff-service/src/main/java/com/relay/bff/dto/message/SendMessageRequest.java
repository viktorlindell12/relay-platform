package com.relay.bff.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SendMessageRequest(
        @NotBlank @Size(max = 100) String channel,
        @NotBlank @Size(max = 4000) String content
) {}