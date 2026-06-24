package com.ecommerce.orders.infrastructure.messaging;

import com.ecommerce.orders.application.port.out.DomainEventPublisher;
import com.ecommerce.orders.domain.model.event.OrderConfirmedEvent;
import com.ecommerce.orders.domain.model.event.OrderCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Publica los eventos de dominio del pedido hacia RabbitMQ, mapeando cada uno
 * a su routing key en orders.exchange.
 */
@Component
public class RabbitMQEventPublisher implements DomainEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(Object event) {
        if (event instanceof OrderCreatedEvent created) {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDERS_EXCHANGE,
                    RabbitMQConfig.ORDER_CREATED_ROUTING_KEY, created);
        } else if (event instanceof OrderConfirmedEvent confirmed) {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDERS_EXCHANGE,
                    RabbitMQConfig.ORDER_CONFIRMED_ROUTING_KEY, confirmed);
        }
    }
}
