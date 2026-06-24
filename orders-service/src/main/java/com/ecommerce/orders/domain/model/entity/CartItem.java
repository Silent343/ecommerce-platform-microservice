package com.ecommerce.orders.domain.model.entity;

import com.ecommerce.orders.domain.model.valueobject.Money;

import java.util.Objects;
import java.util.UUID;

/**
 * Ítem dentro del carrito. Guarda una "foto" del producto (nombre, precio) al
 * momento de agregarlo, además del productId que referencia a products-service.
 */
public class CartItem {

    private final UUID itemId;
    private final UUID productId;
    private final String productName;
    private final Money unitPrice;
    private int quantity;

    public CartItem(UUID itemId, UUID productId, String productName, Money unitPrice, int quantity) {
        if (productId == null) {
            throw new IllegalArgumentException("El productId es obligatorio");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        this.itemId = itemId != null ? itemId : UUID.randomUUID();
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public static CartItem create(UUID productId, String productName, Money unitPrice, int quantity) {
        return new CartItem(UUID.randomUUID(), productId, productName, unitPrice, quantity);
    }

    public void increaseQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("La cantidad a aumentar debe ser positiva");
        }
        this.quantity += amount;
    }

    public Money subtotal() {
        return unitPrice.multiply(quantity);
    }

    public UUID getItemId() {
        return itemId;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem cartItem)) return false;
        return itemId.equals(cartItem.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }
}
