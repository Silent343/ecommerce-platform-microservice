package com.ecommerce.orders.application.port.in;

import com.ecommerce.orders.application.dto.command.AddCartItemCommand;
import com.ecommerce.orders.application.dto.response.CartResponse;

import java.util.UUID;

public interface ManageCartUseCase {
    CartResponse getCart(UUID customerId);
    CartResponse addItem(AddCartItemCommand command);
    CartResponse removeItem(UUID customerId, UUID itemId);
}
