package com.ecommerce.orders.domain.exception;

public class EmptyCartException extends RuntimeException {
    public EmptyCartException(String detail) {
        super("No se puede hacer checkout de un carrito vacío" + (detail != null ? ": " + detail : ""));
    }

    public EmptyCartException() {
        super("No se puede hacer checkout de un carrito vacío");
    }
}
