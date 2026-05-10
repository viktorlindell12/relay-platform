package com.relay.message.event;

import com.relay.message.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Publishes domain events to the RabbitMQ topic exchange after message operations.
 */
@Component
public class MessageEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(MessageEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public MessageEventPublisher(RabbitTemplate rabbitTemplate,
                                  @Value("${relay.events.exchange}") String exchange,
                                  @Value("${relay.events.routing-key.message-published}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    /**
     * Publishes a {@code message.published} event so downstream consumers (e.g. relay-bot-service)
     * can react to new messages without polling the database.
     *
     * @param message the message that was just persisted
     */
    public void publishMessageSent(Message message) {
        MessageSentEvent event = new MessageSentEvent(
                message.getId(),
                message.getSenderId(),
                message.getChannel(),
                message.getContent(),
                message.getCreatedAt()
        );
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        log.debug("Published message.published event for message id={}", message.getId());
    }
}