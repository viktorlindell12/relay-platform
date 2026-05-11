package com.relay.bot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Externalized configuration for the bot's identity and reply behaviour.
 *
 * @param userId       authUserId the bot uses when posting replies — must match a valid user in User Service
 * @param replyContent fixed reply text posted on every received message
 * @param replyDelayMs milliseconds to wait before posting the reply (demonstrates async nature)
 */
@ConfigurationProperties("relay.bot")
public record BotProperties(Long userId, String replyContent, long replyDelayMs) {}