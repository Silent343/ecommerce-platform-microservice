package com.ecommerce.orders.domain.model.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Evento emitido cuando el pedido se confirma tras un pago exitoso.
 * Lo consume notification-service para avisar al cliente.
 */
public record OrderConfirmedEvent(
        UUID orderId,
        UUID customerId,
        Instant occurredOn
) {
}
