package com.ecommerce.orders.application.dto.external;

import java.time.Instant;
import java.util.UUID;

/**
 * Evento que publica payment-service y que orders-service consume.
 * 'status' será "COMPLETED" o "FAILED". Es un contrato compartido entre ambos
 * servicios (mismo shape al serializar/deserializar JSON).
 */
public record PaymentCompletedEvent(
        UUID orderId,
        UUID paymentId,
        String status,
        Instant occurredOn
) {
}
