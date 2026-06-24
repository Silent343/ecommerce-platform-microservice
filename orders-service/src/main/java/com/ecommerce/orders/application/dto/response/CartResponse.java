package com.ecommerce.orders.application.dto.response;

import com.ecommerce.orders.domain.model.aggregate.Cart;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartResponse(
        UUID cartId,
        UUID customerId,
        List<CartItemResponse> items,
        BigDecimal total,
        String currency
) {
    public static CartResponse from(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(CartItemResponse::from)
                .toList();
        return new CartResponse(
                cart.getId().value(),
                cart.getCustomerId().value(),
                items,
                cart.total().amount(),
                cart.total().currency());
    }
}
