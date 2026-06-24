package com.ecommerce.products.application.port.in;

import java.util.List;

/**
 * Puerto de salida para publicar eventos de dominio al message broker.
 * La implementación concreta (RabbitMQ) vive en infrastructure/messaging.
 */
public interface DomainEventPublisher {
    void publish(Object event);

    default void publishAll(List<Object> events) {
        events.forEach(this::publish);
    }
}
