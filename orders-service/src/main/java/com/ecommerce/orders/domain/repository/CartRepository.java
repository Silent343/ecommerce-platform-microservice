package com.ecommerce.orders.domain.repository;

import com.ecommerce.orders.domain.model.aggregate.Cart;
import com.ecommerce.orders.domain.model.valueobject.CustomerId;

import java.util.Optional;

public interface CartRepository {
    Cart save(Cart cart);
    Optional<Cart> findByCustomerId(CustomerId customerId);
}
