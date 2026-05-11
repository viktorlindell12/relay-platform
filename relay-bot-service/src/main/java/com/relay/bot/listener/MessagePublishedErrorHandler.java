package com.relay.bot.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * Handles errors thrown during {@code message.published} event processing.
 * Logs and discards malformed events so a bad payload never crashes the consumer.
 */
@Component("messagePublishedErrorHandler")
public class MessagePublishedErrorHandler implements RabbitListenerErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(MessagePublishedErrorHandler.class);

    /**
     * Logs the raw message body and the cause, then returns {@code null} to acknowledge
     * and discard the message without requeuing.
     *
     * @param amqpMessage the raw AMQP message
     * @param message     the Spring messaging wrapper (may be {@code null} if conversion failed)
     * @param exception   the exception thrown by the listener or message converter
     * @return {@code null} — signals acknowledgement without result
     */
    @Override
    public Object handleError(Message amqpMessage,
                              org.springframework.messaging.Message<?> message,
                              ListenerExecutionFailedException exception) {
        String body = new String(amqpMessage.getBody(), StandardCharsets.UTF_8);
        Throwable cause = exception.getCause() != null ? exception.getCause() : exception;
        log.warn("Discarding malformed message-published event body=[{}]: {}", body, cause.getMessage());
        return null;
    }
}