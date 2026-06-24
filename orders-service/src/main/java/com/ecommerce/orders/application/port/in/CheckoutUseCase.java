package com.ecommerce.orders.application.port.in;

import com.ecommerce.orders.application.dto.command.CheckoutCommand;
import com.ecommerce.orders.application.dto.response.OrderResponse;

/**
 * Caso de uso del checkout: convierte el carrito en un pedido, verificando
 * stock contra products-service y emitiendo el evento order.created.
 */
public interface CheckoutUseCase {
    OrderResponse checkout(CheckoutCommand command);
}
