package com.ecommerce.orders.domain.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String detail) {
        super("Pedido no encontrado" + (detail != null ? ": " + detail : ""));
    }

    public OrderNotFoundException() {
        super("Pedido no encontrado");
    }
}
