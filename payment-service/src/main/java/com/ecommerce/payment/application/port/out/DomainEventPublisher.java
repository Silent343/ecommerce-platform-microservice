package com.ecommerce.payment.application.port.out;

import java.util.List;

public interface DomainEventPublisher {
    void publish(Object event);

    default void publishAll(List<Object> events) {
        events.forEach(this::publish);
    }
}
