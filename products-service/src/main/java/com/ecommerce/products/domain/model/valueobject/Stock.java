package com.ecommerce.products.domain.model.valueobject;

import com.ecommerce.products.domain.exception.InsufficientStockException;

import java.util.Objects;

/**
 * Value Object que representa la cantidad disponible en inventario.
 * Es inmutable: las operaciones devuelven una nueva instancia.
 * Protege el invariante de que el stock nunca puede ser negativo.
 */
public final class Stock {

    private final int quantity;

    private Stock(int quantity) {
        this.quantity = quantity;
    }

    public static Stock of(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        return new Stock(quantity);
    }

    public static Stock empty() {
        return new Stock(0);
    }

    public Stock increase(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("La cantidad a aumentar debe ser positiva");
        }
        return new Stock(this.quantity + amount);
    }

    public Stock decrease(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("La cantidad a descontar debe ser positiva");
        }
        if (amount > this.quantity) {
            throw new InsufficientStockException(this.quantity, amount);
        }
        return new Stock(this.quantity - amount);
    }

    public boolean isAvailable(int amount) {
        return this.quantity >= amount;
    }

    public boolean isOutOfStock() {
        return this.quantity == 0;
    }

    public int quantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stock stock)) return false;
        return quantity == stock.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
