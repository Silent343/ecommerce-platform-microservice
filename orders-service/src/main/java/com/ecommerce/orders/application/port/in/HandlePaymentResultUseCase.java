package com.ecommerce.orders.application.port.in;

import com.ecommerce.orders.application.dto.external.PaymentCompletedEvent;

/**
 * Caso de uso disparado al recibir el resultado de un pago desde payment-service.
 * Confirma o marca como fallido el pedido correspondiente.
 */
public interface HandlePaymentResultUseCase {
    void handle(PaymentCompletedEvent event);
}
