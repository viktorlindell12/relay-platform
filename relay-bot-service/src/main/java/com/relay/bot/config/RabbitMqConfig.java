package com.relay.bot.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declares the RabbitMQ queue, exchange, and binding for the bot service.
 * The queue is durable so pending events survive broker restarts.
 */
@Configuration
public class RabbitMqConfig {

    @Value("${relay.events.exchange}")
    private String exchange;

    @Value("${relay.events.routing-key.message-published}")
    private String messagePublishedRoutingKey;

    @Value("${relay.events.queue.message-published}")
    private String messagePublishedQueue;

    /** @return the topic exchange that relay services publish events to */
    @Bean
    public TopicExchange relayEventsExchange() {
        return new TopicExchange(exchange, true, false);
    }

    /** @return durable queue that receives {@code message.published} events */
    @Bean
    public Queue messagePublishedQueue() {
        return new Queue(messagePublishedQueue, true);
    }

    /**
     * @param queue    the {@link #messagePublishedQueue()} bean
     * @param exchange the {@link #relayEventsExchange()} bean
     * @return binding between the queue and the exchange on the {@code message.published} routing key
     */
    @Bean
    public Binding messagePublishedBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(messagePublishedRoutingKey);
    }

    /**
     * Replaces the default Java-serialization converter with JSON so all incoming
     * events are deserialized via Jackson.
     *
     * @return Jackson-based AMQP message converter
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}