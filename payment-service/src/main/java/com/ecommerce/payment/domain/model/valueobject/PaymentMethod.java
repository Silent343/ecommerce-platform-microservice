package com.ecommerce.payment.domain.model.valueobject;

/**
 * Métodos de pago soportados. Incluye los más usados en Perú:
 * Yape y Plin (billeteras móviles), además de tarjeta y transferencia.
 */
public enum PaymentMethod {
    CARD,
    YAPE,
    PLIN,
    BANK_TRANSFER,
    CASH
}
