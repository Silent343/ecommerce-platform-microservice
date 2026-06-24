package com.ecommerce.payment.application.dto.external;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Evento que publica orders-service al crear un pedido. payment-service lo
 * consume para iniciar el cobro. Su shape coincide con el que orders emite
 * (orderId, customerId, totalAmount, currency, occurredOn).
 */
public record OrderCreatedEvent(
        UUID orderId,
        UUID customerId,
        BigDecimal totalAmount,
        String currency,
        Instant occurredOn
) {
}
