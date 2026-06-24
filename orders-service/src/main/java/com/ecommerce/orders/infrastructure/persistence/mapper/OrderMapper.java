package com.ecommerce.orders.infrastructure.persistence.mapper;

import com.ecommerce.orders.domain.model.aggregate.Order;
import com.ecommerce.orders.domain.model.entity.OrderItem;
import com.ecommerce.orders.domain.model.valueobject.CustomerId;
import com.ecommerce.orders.domain.model.valueobject.Money;
import com.ecommerce.orders.domain.model.valueobject.OrderId;
import com.ecommerce.orders.domain.model.valueobject.OrderStatus;
import com.ecommerce.orders.infrastructure.persistence.entity.OrderItemJpaEntity;
import com.ecommerce.orders.infrastructure.persistence.entity.OrderJpaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {

    public OrderJpaEntity toJpa(Order order) {
        OrderJpaEntity entity = new OrderJpaEntity(
                order.getId().value(), order.getCustomerId().value(),
                order.getStatus().name(), order.getTotal().amount(),
                order.getTotal().currency(), order.getCreatedAt(), order.getUpdatedAt());

        List<OrderItemJpaEntity> items = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            items.add(new OrderItemJpaEntity(
                    item.getItemId(), item.getProductId(), item.getProductName(),
                    item.getUnitPrice().amount(), item.getUnitPrice().currency(),
                    item.getQuantity(), entity));
        }
        entity.setItems(items);
        return entity;
    }

    public Order toDomain(OrderJpaEntity entity) {
        List<OrderItem> items = new ArrayList<>();
        for (OrderItemJpaEntity i : entity.getItems()) {
            items.add(new OrderItem(
                    i.getId(), i.getProductId(), i.getProductName(),
                    Money.of(i.getUnitPrice(), i.getCurrency()), i.getQuantity()));
        }
        return Order.reconstitute(
                OrderId.of(entity.getId()), CustomerId.of(entity.getCustomerId()),
                items, OrderStatus.valueOf(entity.getStatus()),
                Money.of(entity.getTotalAmount(), entity.getCurrency()),
                entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
