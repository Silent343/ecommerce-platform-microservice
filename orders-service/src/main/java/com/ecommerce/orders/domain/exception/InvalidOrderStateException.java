package com.ecommerce.orders.domain.exception;

public class InvalidOrderStateException extends RuntimeException {
    public InvalidOrderStateException(String detail) {
        super("Operación inválida para el estado actual del pedido" + (detail != null ? ": " + detail : ""));
    }

    public InvalidOrderStateException() {
        super("Operación inválida para el estado actual del pedido");
    }
}
