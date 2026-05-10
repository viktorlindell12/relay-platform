package com.relay.message.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declares the RabbitMQ exchange and configures JSON serialization for all published events.
 * The exchange is durable so it survives broker restarts.
 */
@Configuration
public class RabbitMqConfig {

    @Value("${relay.events.exchange}")
    private String exchange;

    /**
     * @return the topic exchange that all relay event publishers write to
     */
    @Bean
    public TopicExchange relayEventsExchange() {
        return new TopicExchange(exchange, true, false);
    }

    /**
     * Replaces the default {@code SimpleMessageConverter} (Java serialization) with JSON.
     * Without this, sending a POJO via {@code RabbitTemplate} throws {@code IllegalArgumentException}.
     *
     * @return Jackson-based converter that serializes events as {@code application/json}
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Wires the JSON converter into the template so all {@code convertAndSend} calls use JSON.
     *
     * @param connectionFactory auto-configured AMQP connection
     * @param converter         the {@link #jsonMessageConverter()} bean
     * @return configured {@link RabbitTemplate}
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}