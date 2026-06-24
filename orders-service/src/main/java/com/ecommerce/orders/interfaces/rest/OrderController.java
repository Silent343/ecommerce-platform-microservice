package com.ecommerce.orders.interfaces.rest;

import com.ecommerce.orders.application.dto.command.CheckoutCommand;
import com.ecommerce.orders.application.dto.response.OrderResponse;
import com.ecommerce.orders.application.port.in.CheckoutUseCase;
import com.ecommerce.orders.application.port.in.QueryOrderUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Pedidos del cliente autenticado")
public class OrderController {

    private final CheckoutUseCase checkoutUseCase;
    private final QueryOrderUseCase queryOrderUseCase;

    public OrderController(CheckoutUseCase checkoutUseCase, QueryOrderUseCase queryOrderUseCase) {
        this.checkoutUseCase = checkoutUseCase;
        this.queryOrderUseCase = queryOrderUseCase;
    }

    @PostMapping("/checkout")
    @Operation(summary = "Convierte el carrito en un pedido (verifica stock y emite order.created)")
    public ResponseEntity<OrderResponse> checkout(Authentication authentication) {
        UUID customerId = UUID.fromString(authentication.getName());
        OrderResponse response = checkoutUseCase.checkout(new CheckoutCommand(customerId));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Lista los pedidos del cliente autenticado")
    public ResponseEntity<List<OrderResponse>> myOrders(Authentication authentication) {
        UUID customerId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(queryOrderUseCase.getByCustomer(customerId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un pedido por su ID")
    public ResponseEntity<OrderResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(queryOrderUseCase.getById(id));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancela un pedido en estado PENDING")
    public ResponseEntity<OrderResponse> cancel(@PathVariable UUID id) {
        return ResponseEntity.ok(queryOrderUseCase.cancel(id));
    }
}
