package com.ecommerce.payment.domain.model.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Evento que publica payment-service tras procesar un cobro.
 * IMPORTANTE: su shape coincide con el que orders-service espera consumir
 * (orderId, paymentId, status, occurredOn). 'status' = "COMPLETED" o "FAILED".
 * Es el contrato compartido que cierra el flujo del pedido.
 */
public record PaymentCompletedEvent(
        UUID orderId,
        UUID paymentId,
        String status,
        Instant occurredOn
) {
}
