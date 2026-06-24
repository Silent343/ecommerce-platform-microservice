package com.ecommerce.orders.domain.model.aggregate;

import com.ecommerce.orders.domain.model.entity.CartItem;
import com.ecommerce.orders.domain.model.valueobject.CartId;
import com.ecommerce.orders.domain.model.valueobject.CustomerId;
import com.ecommerce.orders.domain.model.valueobject.Money;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Agregado raíz del carrito de compras. Cada cliente tiene un carrito.
 * Es la única puerta para agregar o quitar ítems, y mantiene el invariante
 * de que no haya dos ítems del mismo producto (se acumula la cantidad).
 */
public class Cart {

    private final CartId id;
    private final CustomerId customerId;
    private final List<CartItem> items;
    private final Instant createdAt;
    private Instant updatedAt;

    private Cart(CartId id, CustomerId customerId, List<CartItem> items,
                 Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.items = items != null ? items : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Cart createFor(CustomerId customerId) {
        Instant now = Instant.now();
        return new Cart(CartId.generate(), customerId, new ArrayList<>(), now, now);
    }

    public static Cart reconstitute(CartId id, CustomerId customerId, List<CartItem> items,
                                    Instant createdAt, Instant updatedAt) {
        return new Cart(id, customerId, items, createdAt, updatedAt);
    }

    /**
     * Agrega un producto. Si ya existe en el carrito, acumula la cantidad en
     * lugar de duplicar la línea.
     */
    public void addItem(UUID productId, String productName, Money unitPrice, int quantity) {
        Optional<CartItem> existing = items.stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().increaseQuantity(quantity);
        } else {
            items.add(CartItem.create(productId, productName, unitPrice, quantity));
        }
        this.touch();
    }

    public void removeItem(UUID itemId) {
        items.removeIf(i -> i.getItemId().equals(itemId));
        this.touch();
    }

    public void clear() {
        items.clear();
        this.touch();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public Money total() {
        return items.stream()
                .map(CartItem::subtotal)
                .reduce(Money.zero(), Money::add);
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    public CartId getId() {
        return id;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
