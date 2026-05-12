package com.relay.bot.service;

import com.relay.bot.client.MessageServiceClient;
import com.relay.bot.config.BotProperties;
import com.relay.bot.event.MessagePublishedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Generates and posts a bot reply for each incoming {@code message.published} event.
 * The reply is scheduled non-blocking via {@link TaskScheduler} so the RabbitMQ listener
 * thread is released immediately.
 */
@Service
public class BotReplyService {

    private static final Logger log = LoggerFactory.getLogger(BotReplyService.class);

    private final MessageServiceClient messageServiceClient;
    private final BotProperties botProperties;
    private final TaskScheduler taskScheduler;

    public BotReplyService(MessageServiceClient messageServiceClient,
                           BotProperties botProperties,
                           TaskScheduler botReplyScheduler) {
        this.messageServiceClient = messageServiceClient;
        this.botProperties = botProperties;
        this.taskScheduler = botReplyScheduler;
    }

    /**
     * Schedules a reply to the channel the event arrived on after the configured delay.
     * Skips events sent by the bot itself to prevent infinite reply loops.
     * Returns immediately — the actual HTTP call runs on the scheduler thread pool.
     *
     * @param event the received {@code message.published} event
     */
    public void reply(MessagePublishedEvent event) {
        if (botProperties.userId().equals(event.senderId())) {
            log.debug("Skipping reply to own message messageId={}", event.messageId());
            return;
        }

        Instant executeAt = Instant.now().plusMillis(botProperties.replyDelayMs());
        try {
            taskScheduler.schedule(() -> {
                messageServiceClient.postMessage(botProperties.userId(), event.channel(), botProperties.replyContent());
                log.info("Posted reply to channel={} triggered by messageId={}", event.channel(), event.messageId());
            }, executeAt);
        } catch (TaskRejectedException e) {
            log.warn("Reply scheduling rejected for messageId={}: {}", event.messageId(), e.getMessage());
        }
    }
}