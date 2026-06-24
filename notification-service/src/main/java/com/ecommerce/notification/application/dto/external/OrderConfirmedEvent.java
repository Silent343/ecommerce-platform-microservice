package com.ecommerce.notification.application.dto.external;

import java.time.Instant;
import java.util.UUID;

/** Evento publicado por orders-service. Su shape coincide con el emisor. */
public record OrderConfirmedEvent(
        UUID orderId,
        UUID customerId,
        Instant occurredOn
) {
}
