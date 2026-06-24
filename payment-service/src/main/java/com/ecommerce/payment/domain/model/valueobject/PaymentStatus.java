package com.ecommerce.payment.domain.model.valueobject;

/**
 * Estados de un pago.
 *  PENDING   -> creado, aún no procesado
 *  COMPLETED -> cobro exitoso
 *  FAILED    -> el cobro falló
 *  REFUNDED  -> reembolsado tras haber sido completado
 */
public enum PaymentStatus {
    PENDING,
    COMPLETED,
    FAILED,
    REFUNDED
}
