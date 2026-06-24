package com.ecommerce.orders.application.dto.response;

import com.ecommerce.orders.domain.model.aggregate.Order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        UUID customerId,
        List<OrderItemResponse> items,
        String status,
        BigDecimal total,
        String currency,
        Instant createdAt
) {
    public static OrderResponse from(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(OrderItemResponse::from)
                .toList();
        return new OrderResponse(
                order.getId().value(),
                order.getCustomerId().value(),
                items,
                order.getStatus().name(),
                order.getTotal().amount(),
                order.getTotal().currency(),
                order.getCreatedAt());
    }
}
