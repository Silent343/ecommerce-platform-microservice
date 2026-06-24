package com.ecommerce.payment.domain.exception;

public class RefundNotAllowedException extends RuntimeException {
    public RefundNotAllowedException(String reason) {
        super("No se puede reembolsar: " + reason);
    }
}
