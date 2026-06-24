package com.ecommerce.users.application.port.in;

import java.util.List;

/**
 * Puerto de salida para publicar eventos de dominio hacia el message broker.
 * La implementación concreta (RabbitMQ) vive en infrastructure/messaging.
 */
public interface DomainEventPublisher {
    void publish(Object event);

    default void publishAll(List<Object> events) {
        events.forEach(this::publish);
    }
}
