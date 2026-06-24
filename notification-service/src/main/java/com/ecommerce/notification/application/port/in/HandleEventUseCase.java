package com.ecommerce.notification.application.port.in;

import com.ecommerce.notification.application.dto.external.OrderConfirmedEvent;
import com.ecommerce.notification.application.dto.external.PaymentCompletedEvent;

/**
 * Casos de uso disparados por eventos de otros servicios. Cada uno genera y
 * envía la notificación correspondiente al cliente.
 */
public interface HandleEventUseCase {
    void onOrderConfirmed(OrderConfirmedEvent event);
    void onPaymentResult(PaymentCompletedEvent event);
}
