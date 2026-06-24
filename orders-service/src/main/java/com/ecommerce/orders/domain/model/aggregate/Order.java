package com.ecommerce.orders.domain.model.aggregate;

import com.ecommerce.orders.domain.exception.InvalidOrderStateException;
import com.ecommerce.orders.domain.model.entity.OrderItem;
import com.ecommerce.orders.domain.model.event.OrderConfirmedEvent;
import com.ecommerce.orders.domain.model.event.OrderCreatedEvent;
import com.ecommerce.orders.domain.model.valueobject.CustomerId;
import com.ecommerce.orders.domain.model.valueobject.Money;
import com.ecommerce.orders.domain.model.valueobject.OrderId;
import com.ecommerce.orders.domain.model.valueobject.OrderStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Agregado raíz del pedido. Modela una máquina de estados:
 *
 *   PENDING --confirm()--> CONFIRMED
 *   PENDING --markPaymentFailed()--> PAYMENT_FAILED
 *   PENDING --cancel()--> CANCELLED
 *
 * Las transiciones desde estados finales (CONFIRMED, CANCELLED, PAYMENT_FAILED)
 * se rechazan. El agregado acumula eventos de dominio que la capa de aplicación
 * publicará tras persistir.
 */
public class Order {

    private final OrderId id;
    private final CustomerId customerId;
    private final List<OrderItem> items;
    private OrderStatus status;
    private final Money total;
    private final Instant createdAt;
    private Instant updatedAt;

    private final transient List<Object> domainEvents = new ArrayList<>();

    private Order(OrderId id, CustomerId customerId, List<OrderItem> items,
                  OrderStatus status, Money total, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.items = items != null ? items : new ArrayList<>();
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /** Factory que crea un pedido en estado PENDING y emite OrderCreatedEvent. */
    public static Order place(CustomerId customerId, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Un pedido debe tener al menos un ítem");
        }
        OrderId newId = OrderId.generate();
        Instant now = Instant.now();
        Money total = items.stream()
                .map(OrderItem::subtotal)
                .reduce(Money.zero(), Money::add);

        Order order = new Order(newId, customerId, new ArrayList<>(items),
                OrderStatus.PENDING, total, now, now);
        order.registerEvent(new OrderCreatedEvent(
                newId.value(), customerId.value(), total.amount(), total.currency(), now));
        return order;
    }

    public static Order reconstitute(OrderId id, CustomerId customerId, List<OrderItem> items,
                                     OrderStatus status, Money total,
                                     Instant createdAt, Instant updatedAt) {
        return new Order(id, customerId, items, status, total, createdAt, updatedAt);
    }

    public void confirm() {
        requirePending("confirmar");
        this.status = OrderStatus.CONFIRMED;
        this.touch();
        registerEvent(new OrderConfirmedEvent(id.value(), customerId.value(), Instant.now()));
    }

    public void markPaymentFailed() {
        requirePending("marcar como pago fallido");
        this.status = OrderStatus.PAYMENT_FAILED;
        this.touch();
    }

    public void cancel() {
        requirePending("cancelar");
        this.status = OrderStatus.CANCELLED;
        this.touch();
    }

    private void requirePending(String action) {
        if (this.status != OrderStatus.PENDING) {
            throw new InvalidOrderStateException(
                    "No se puede " + action + " un pedido en estado " + this.status);
        }
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    private void registerEvent(Object event) {
        this.domainEvents.add(event);
    }

    public List<Object> pullDomainEvents() {
        List<Object> copy = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return copy;
    }

    public OrderId getId() {
        return id;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Money getTotal() {
        return total;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
