package com.ecommerce.orders.domain.model.entity;

import com.ecommerce.orders.domain.model.valueobject.Money;

import java.util.Objects;
import java.util.UUID;

/**
 * Ítem de un pedido. A diferencia del CartItem, es inmutable: congela el precio
 * y la cantidad al momento del checkout, de modo que cambios posteriores en el
 * catálogo no alteren pedidos ya realizados.
 */
public class OrderItem {

    private final UUID itemId;
    private final UUID productId;
    private final String productName;
    private final Money unitPrice;
    private final int quantity;

    public OrderItem(UUID itemId, UUID productId, String productName, Money unitPrice, int quantity) {
        this.itemId = itemId != null ? itemId : UUID.randomUUID();
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public static OrderItem create(UUID productId, String productName, Money unitPrice, int quantity) {
        return new OrderItem(UUID.randomUUID(), productId, productName, unitPrice, quantity);
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
        if (!(o instanceof OrderItem orderItem)) return false;
        return itemId.equals(orderItem.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }
}
