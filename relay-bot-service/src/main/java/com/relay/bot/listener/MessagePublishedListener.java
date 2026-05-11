package com.relay.bot.listener;

import com.relay.bot.event.MessagePublishedEvent;
import com.relay.bot.service.BotReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumes {@code message.published} events from RabbitMQ.
 * Malformed events are handled by {@link MessagePublishedErrorHandler} and never crash the service.
 */
@Component
public class MessagePublishedListener {

    private static final Logger log = LoggerFactory.getLogger(MessagePublishedListener.class);

    private final BotReplyService botReplyService;

    public MessagePublishedListener(BotReplyService botReplyService) {
        this.botReplyService = botReplyService;
    }

    /**
     * Invoked for every valid {@code message.published} event.
     * Delegates reply generation to {@link BotReplyService} which runs asynchronously.
     *
     * @param event the deserialized event payload
     */
    @RabbitListener(
            queues = "${relay.events.queue.message-published}",
            errorHandler = "messagePublishedErrorHandler"
    )
    public void onMessagePublished(MessagePublishedEvent event) {
        log.info("Received message-published event: messageId={} channel={}", event.messageId(), event.channel());
        botReplyService.reply(event);
    }
}