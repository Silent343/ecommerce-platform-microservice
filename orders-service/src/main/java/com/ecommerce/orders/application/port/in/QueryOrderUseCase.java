package com.ecommerce.orders.application.port.in;

import com.ecommerce.orders.application.dto.response.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface QueryOrderUseCase {
    OrderResponse getById(UUID orderId);
    List<OrderResponse> getByCustomer(UUID customerId);
    OrderResponse cancel(UUID orderId);
}
