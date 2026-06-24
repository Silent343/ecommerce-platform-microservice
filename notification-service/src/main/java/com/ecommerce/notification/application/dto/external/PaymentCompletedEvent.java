package com.ecommerce.notification.application.dto.external;

import java.time.Instant;
import java.util.UUID;

/**
 * Evento publicado por payment-service. 'status' = COMPLETED o FAILED.
 * notification reacciona principalmente al caso FAILED para avisar al cliente.
 */
public record PaymentCompletedEvent(
        UUID orderId,
        UUID paymentId,
        String status,
        Instant occurredOn
) {
}
