package com.ecommerce.payment.infrastructure.messaging;

import com.ecommerce.payment.application.port.out.DomainEventPublisher;
import com.ecommerce.payment.domain.model.event.PaymentCompletedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Publica los eventos de dominio del pago hacia RabbitMQ. El payment.completed
 * (tanto en éxito como en fallo) sale por payments.exchange y lo consume
 * orders-service.
 */
@Component
public class RabbitMQEventPublisher implements DomainEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(Object event) {
        if (event instanceof PaymentCompletedEvent completed) {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.PAYMENTS_EXCHANGE,
                    RabbitMQConfig.PAYMENT_COMPLETED_ROUTING_KEY, completed);
        }
    }
}
