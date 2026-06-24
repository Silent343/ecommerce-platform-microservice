package com.ecommerce.payment.domain.exception;

public class PaymentAlreadyProcessedException extends RuntimeException {
    public PaymentAlreadyProcessedException(String id) {
        super("El pago ya fue procesado: " + id);
    }
}
