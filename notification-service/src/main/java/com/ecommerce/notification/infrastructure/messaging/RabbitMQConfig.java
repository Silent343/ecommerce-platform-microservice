package com.ecommerce.notification.infrastructure.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ del servicio de notificaciones. Es puramente consumidor: se suscribe
 * a eventos de orders y payment para avisar al cliente.
 *
 * Consume:
 *   - order.confirmed   (de orders.exchange)
 *   - payment.completed (de payments.exchange)
 */
@Configuration
public class RabbitMQConfig {

    public static final String ORDERS_EXCHANGE = "orders.exchange";
    public static final String PAYMENTS_EXCHANGE = "payments.exchange";

    public static final String ORDER_CONFIRMED_ROUTING_KEY = "order.confirmed";
    public static final String PAYMENT_COMPLETED_ROUTING_KEY = "payment.completed";

    public static final String ORDER_CONFIRMED_QUEUE = "order.confirmed.notifications.queue";
    public static final String PAYMENT_COMPLETED_QUEUE = "payment.completed.notifications.queue";

    @Bean
    public TopicExchange ordersExchange() {
        return new TopicExchange(ORDERS_EXCHANGE);
    }

    @Bean
    public TopicExchange paymentsExchange() {
        return new TopicExchange(PAYMENTS_EXCHANGE);
    }

    @Bean
    public Queue orderConfirmedQueue() {
        return new Queue(ORDER_CONFIRMED_QUEUE, true);
    }

    @Bean
    public Queue paymentCompletedQueue() {
        return new Queue(PAYMENT_COMPLETED_QUEUE, true);
    }

    @Bean
    public Binding orderConfirmedBinding(Queue orderConfirmedQueue, TopicExchange ordersExchange) {
        return BindingBuilder.bind(orderConfirmedQueue).to(ordersExchange)
                .with(ORDER_CONFIRMED_ROUTING_KEY);
    }

    @Bean
    public Binding paymentCompletedBinding(Queue paymentCompletedQueue, TopicExchange paymentsExchange) {
        return BindingBuilder.bind(paymentCompletedQueue).to(paymentsExchange)
                .with(PAYMENT_COMPLETED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
