package com.relay.bot.config;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Externalized configuration for the bot's identity and reply behaviour.
 *
 * @param userId       authUserId the bot uses when posting replies — must match a valid user in User Service
 * @param replyContent fixed reply text posted on every received message
 * @param replyDelayMs milliseconds to wait before posting the reply (demonstrates async nature)
 */
@Validated
@ConfigurationProperties("relay.bot")
public record BotProperties(
        @NotNull Long userId,
        String replyContent,
        @PositiveOrZero long replyDelayMs) {}