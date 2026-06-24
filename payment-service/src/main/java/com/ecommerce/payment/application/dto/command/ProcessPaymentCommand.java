package com.ecommerce.payment.application.dto.command;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Command para procesar un pago manualmente (vía REST). Cuando el pago se
 * dispara por el evento order.created, este command se arma a partir del evento.
 */
public record ProcessPaymentCommand(
        UUID orderId,
        UUID customerId,
        BigDecimal amount,
        String currency,
        String method
) {
}
