package com.ecommerce.orders.interfaces.rest;

import com.ecommerce.orders.application.dto.command.AddCartItemCommand;
import com.ecommerce.orders.application.dto.response.CartResponse;
import com.ecommerce.orders.application.port.in.ManageCartUseCase;
import com.ecommerce.orders.interfaces.rest.request.AddCartItemRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * API del carrito. El cliente se identifica con el subject del token JWT
 * (no se pasa por parámetro), de modo que cada quien solo opera su propio carrito.
 */
@RestController
@RequestMapping("/api/cart")
@Tag(name = "Cart", description = "Carrito de compras del cliente autenticado")
public class CartController {

    private final ManageCartUseCase manageCartUseCase;

    public CartController(ManageCartUseCase manageCartUseCase) {
        this.manageCartUseCase = manageCartUseCase;
    }

    @GetMapping
    @Operation(summary = "Obtiene el carrito del cliente autenticado")
    public ResponseEntity<CartResponse> getCart(Authentication authentication) {
        UUID customerId = currentCustomer(authentication);
        return ResponseEntity.ok(manageCartUseCase.getCart(customerId));
    }

    @PostMapping("/items")
    @Operation(summary = "Agrega un producto al carrito")
    public ResponseEntity<CartResponse> addItem(
            Authentication authentication,
            @Valid @RequestBody AddCartItemRequest request) {
        UUID customerId = currentCustomer(authentication);
        AddCartItemCommand command = new AddCartItemCommand(
                customerId, request.productId(), request.quantity());
        return ResponseEntity.ok(manageCartUseCase.addItem(command));
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Quita un ítem del carrito")
    public ResponseEntity<CartResponse> removeItem(
            Authentication authentication,
            @PathVariable UUID itemId) {
        UUID customerId = currentCustomer(authentication);
        return ResponseEntity.ok(manageCartUseCase.removeItem(customerId, itemId));
    }

    private UUID currentCustomer(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}
