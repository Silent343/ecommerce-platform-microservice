package com.ecommerce.products.domain.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(int available, int requested) {
        super("Stock insuficiente. Disponible: " + available + ", solicitado: " + requested);
    }
}
