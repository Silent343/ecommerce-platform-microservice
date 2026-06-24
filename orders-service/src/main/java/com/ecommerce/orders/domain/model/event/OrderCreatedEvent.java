package com.ecommerce.orders.domain.model.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Evento emitido al crear un pedido (estado PENDING). Lo consume payment-service
 * para iniciar el cobro. Incluye el monto total a cobrar.
 */
public record OrderCreatedEvent(
        UUID orderId,
        UUID customerId,
        BigDecimal totalAmount,
        String currency,
        Instant occurredOn
) {
}
