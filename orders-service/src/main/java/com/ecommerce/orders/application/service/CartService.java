package com.ecommerce.orders.application.service;

import com.ecommerce.orders.application.dto.command.AddCartItemCommand;
import com.ecommerce.orders.application.dto.response.CartResponse;
import com.ecommerce.orders.application.port.in.ManageCartUseCase;
import com.ecommerce.orders.application.port.out.ProductCatalogPort;
import com.ecommerce.orders.domain.model.aggregate.Cart;
import com.ecommerce.orders.domain.model.valueobject.CustomerId;
import com.ecommerce.orders.domain.model.valueobject.Money;
import com.ecommerce.orders.domain.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Servicio de aplicación del carrito. Al agregar un ítem consulta a
 * products-service (vía ProductCatalogPort) para traer el nombre y precio
 * actuales del producto y validar que haya stock disponible.
 */
@Service
public class CartService implements ManageCartUseCase {

    private final CartRepository cartRepository;
    private final ProductCatalogPort productCatalog;

    public CartService(CartRepository cartRepository, ProductCatalogPort productCatalog) {
        this.cartRepository = cartRepository;
        this.productCatalog = productCatalog;
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(UUID customerId) {
        Cart cart = findOrCreateCart(CustomerId.of(customerId));
        return CartResponse.from(cart);
    }

    @Override
    @Transactional
    public CartResponse addItem(AddCartItemCommand command) {
        // Consulta síncrona a products-service: precio y stock actuales
        ProductCatalogPort.ProductInfo product = productCatalog.getProduct(command.productId());

        if (!productCatalog.hasAvailableStock(command.productId(), command.quantity())) {
            throw new IllegalArgumentException(
                    "Stock insuficiente para el producto " + product.name());
        }

        Cart cart = findOrCreateCart(CustomerId.of(command.customerId()));
        Money unitPrice = Money.of(product.price(), product.currency());
        cart.addItem(command.productId(), product.name(), unitPrice, command.quantity());

        return CartResponse.from(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartResponse removeItem(UUID customerId, UUID itemId) {
        Cart cart = findOrCreateCart(CustomerId.of(customerId));
        cart.removeItem(itemId);
        return CartResponse.from(cartRepository.save(cart));
    }

    private Cart findOrCreateCart(CustomerId customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> cartRepository.save(Cart.createFor(customerId)));
    }
}
