package com.ecommerce.orders.infrastructure.persistence.mapper;

import com.ecommerce.orders.domain.model.aggregate.Cart;
import com.ecommerce.orders.domain.model.entity.CartItem;
import com.ecommerce.orders.domain.model.valueobject.CartId;
import com.ecommerce.orders.domain.model.valueobject.CustomerId;
import com.ecommerce.orders.domain.model.valueobject.Money;
import com.ecommerce.orders.infrastructure.persistence.entity.CartItemJpaEntity;
import com.ecommerce.orders.infrastructure.persistence.entity.CartJpaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartMapper {

    public CartJpaEntity toJpa(Cart cart) {
        CartJpaEntity entity = new CartJpaEntity(
                cart.getId().value(), cart.getCustomerId().value(),
                cart.getCreatedAt(), cart.getUpdatedAt());

        List<CartItemJpaEntity> items = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            items.add(new CartItemJpaEntity(
                    item.getItemId(), item.getProductId(), item.getProductName(),
                    item.getUnitPrice().amount(), item.getUnitPrice().currency(),
                    item.getQuantity(), entity));
        }
        entity.setItems(items);
        return entity;
    }

    public Cart toDomain(CartJpaEntity entity) {
        List<CartItem> items = new ArrayList<>();
        for (CartItemJpaEntity i : entity.getItems()) {
            items.add(new CartItem(
                    i.getId(), i.getProductId(), i.getProductName(),
                    Money.of(i.getUnitPrice(), i.getCurrency()), i.getQuantity()));
        }
        return Cart.reconstitute(
                CartId.of(entity.getId()), CustomerId.of(entity.getCustomerId()),
                items, entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
