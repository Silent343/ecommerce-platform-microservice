package com.ecommerce.products.infrastructure.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de RabbitMQ para los eventos del catálogo.
 * Usa un topic exchange con routing keys "product.created" y "stock.updated".
 */
@Configuration
public class RabbitMQConfig {

    public static final String PRODUCTS_EXCHANGE = "products.exchange";

    public static final String PRODUCT_CREATED_QUEUE = "product.created.queue";
    public static final String PRODUCT_CREATED_ROUTING_KEY = "product.created";

    public static final String STOCK_UPDATED_QUEUE = "stock.updated.queue";
    public static final String STOCK_UPDATED_ROUTING_KEY = "stock.updated";

    @Bean
    public TopicExchange productsExchange() {
        return new TopicExchange(PRODUCTS_EXCHANGE);
    }

    @Bean
    public Queue productCreatedQueue() {
        return new Queue(PRODUCT_CREATED_QUEUE, true);
    }

    @Bean
    public Queue stockUpdatedQueue() {
        return new Queue(STOCK_UPDATED_QUEUE, true);
    }

    @Bean
    public Binding productCreatedBinding(Queue productCreatedQueue, TopicExchange productsExchange) {
        return BindingBuilder.bind(productCreatedQueue).to(productsExchange)
                .with(PRODUCT_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding stockUpdatedBinding(Queue stockUpdatedQueue, TopicExchange productsExchange) {
        return BindingBuilder.bind(stockUpdatedQueue).to(productsExchange)
                .with(STOCK_UPDATED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
