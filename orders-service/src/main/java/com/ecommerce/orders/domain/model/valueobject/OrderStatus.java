package com.ecommerce.orders.domain.model.valueobject;

/**
 * Estados del ciclo de vida de un pedido.
 *  PENDING        -> creado, esperando confirmación de pago
 *  CONFIRMED      -> pago exitoso, stock descontado
 *  PAYMENT_FAILED -> el pago falló
 *  CANCELLED      -> cancelado por el cliente antes de confirmarse
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PAYMENT_FAILED,
    CANCELLED
}
