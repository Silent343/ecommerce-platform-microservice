package com.ecommerce.users.infrastructure.messaging;

import com.ecommerce.users.application.port.in.DomainEventPublisher;
import com.ecommerce.users.domain.model.event.UserRegisteredEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Adaptador que publica eventos de dominio hacia RabbitMQ.
 * Mapea cada tipo de evento a su routing key correspondiente.
 */
@Component
public class RabbitMQEventPublisher implements DomainEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(Object event) {
        if (event instanceof UserRegisteredEvent userRegistered) {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.USERS_EXCHANGE,
                    RabbitMQConfig.USER_REGISTERED_ROUTING_KEY,
                    userRegistered);
        }
        // Otros eventos del dominio se mapean aquí a medida que se agreguen.
    }
}
