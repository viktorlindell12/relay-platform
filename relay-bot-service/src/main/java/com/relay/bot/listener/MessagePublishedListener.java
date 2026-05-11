package com.relay.bot.listener;

import com.relay.bot.event.MessagePublishedEvent;
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

    /**
     * Invoked for every valid {@code message.published} event.
     *
     * @param event the deserialized event payload
     */
    @RabbitListener(
            queues = "${relay.events.queue.message-published}",
            errorHandler = "messagePublishedErrorHandler"
    )
    public void onMessagePublished(MessagePublishedEvent event) {
        log.info("Received message-published event: messageId={} channel={} senderId={}",
                event.messageId(), event.channel(), event.senderId());
    }
}