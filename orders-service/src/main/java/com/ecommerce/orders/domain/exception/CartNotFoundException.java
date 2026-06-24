package com.ecommerce.orders.domain.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String detail) {
        super("Carrito no encontrado" + (detail != null ? ": " + detail : ""));
    }

    public CartNotFoundException() {
        super("Carrito no encontrado");
    }
}
