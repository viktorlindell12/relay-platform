package com.relay.bff.dto.message;

import java.time.Instant;

public record MessageResponse(Long id, String senderDisplayName, String channel, String content, Instant createdAt) {}