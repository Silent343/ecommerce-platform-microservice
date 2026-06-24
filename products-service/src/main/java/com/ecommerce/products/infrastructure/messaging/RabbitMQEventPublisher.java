package com.ecommerce.products.infrastructure.messaging;

import com.ecommerce.products.application.port.in.DomainEventPublisher;
import com.ecommerce.products.domain.model.event.ProductCreatedEvent;
import com.ecommerce.products.domain.model.event.StockUpdatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Adaptador que publica los eventos de dominio del catálogo hacia RabbitMQ,
 * mapeando cada tipo de evento a su routing key.
 */
@Component
public class RabbitMQEventPublisher implements DomainEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(Object event) {
        if (event instanceof ProductCreatedEvent created) {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.PRODUCTS_EXCHANGE,
                    RabbitMQConfig.PRODUCT_CREATED_ROUTING_KEY, created);
        } else if (event instanceof StockUpdatedEvent stockUpdated) {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.PRODUCTS_EXCHANGE,
                    RabbitMQConfig.STOCK_UPDATED_ROUTING_KEY, stockUpdated);
        }
    }
}
