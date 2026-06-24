package com.ecommerce.orders.application.service;

import com.ecommerce.orders.application.dto.command.CheckoutCommand;
import com.ecommerce.orders.application.dto.external.PaymentCompletedEvent;
import com.ecommerce.orders.application.dto.response.OrderResponse;
import com.ecommerce.orders.application.port.in.CheckoutUseCase;
import com.ecommerce.orders.application.port.in.HandlePaymentResultUseCase;
import com.ecommerce.orders.application.port.in.QueryOrderUseCase;
import com.ecommerce.orders.application.port.out.DomainEventPublisher;
import com.ecommerce.orders.application.port.out.ProductCatalogPort;
import com.ecommerce.orders.domain.exception.EmptyCartException;
import com.ecommerce.orders.domain.exception.OrderNotFoundException;
import com.ecommerce.orders.domain.model.aggregate.Cart;
import com.ecommerce.orders.domain.model.aggregate.Order;
import com.ecommerce.orders.domain.model.entity.CartItem;
import com.ecommerce.orders.domain.model.entity.OrderItem;
import com.ecommerce.orders.domain.model.valueobject.CustomerId;
import com.ecommerce.orders.domain.model.valueobject.OrderId;
import com.ecommerce.orders.domain.repository.CartRepository;
import com.ecommerce.orders.domain.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Servicio que orquesta el ciclo de vida del pedido. Combina dos estilos de
 * comunicación entre servicios:
 *
 *  - Síncrono (checkout): verifica stock contra products-service en tiempo real.
 *  - Asíncrono (eventos): publica order.created / order.confirmed y reacciona a
 *    payment.completed.
 *
 * La lógica de negocio vive en los agregados (Order, Cart); este servicio solo
 * coordina: lee, invoca el dominio, persiste y publica eventos.
 */
@Service
public class OrderService implements CheckoutUseCase, QueryOrderUseCase, HandlePaymentResultUseCase {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductCatalogPort productCatalog;
    private final DomainEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository,
                        CartRepository cartRepository,
                        ProductCatalogPort productCatalog,
                        DomainEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productCatalog = productCatalog;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public OrderResponse checkout(CheckoutCommand command) {
        CustomerId customerId = CustomerId.of(command.customerId());
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new EmptyCartException("El cliente no tiene carrito"));

        if (cart.isEmpty()) {
            throw new EmptyCartException();
        }

        // Verificación síncrona de stock contra products-service para cada ítem
        for (CartItem item : cart.getItems()) {
            if (!productCatalog.hasAvailableStock(item.getProductId(), item.getQuantity())) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para el producto " + item.getProductName());
            }
        }

        // El carrito se convierte en ítems de pedido (congelando precio y cantidad)
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(i -> OrderItem.create(
                        i.getProductId(), i.getProductName(), i.getUnitPrice(), i.getQuantity()))
                .toList();

        Order order = Order.place(customerId, orderItems);
        Order saved = orderRepository.save(order);

        // Vacía el carrito tras generar el pedido
        cart.clear();
        cartRepository.save(cart);

        // Publica order.created -> lo consumirá payment-service
        eventPublisher.publishAll(order.pullDomainEvents());

        log.info("Pedido {} creado en estado PENDING, esperando pago", saved.getId());
        return OrderResponse.from(saved);
    }

    @Override
    @Transactional
    public void handle(PaymentCompletedEvent event) {
        Order order = orderRepository.findById(OrderId.of(event.orderId()))
                .orElseThrow(() -> new OrderNotFoundException(event.orderId().toString()));

        if ("COMPLETED".equalsIgnoreCase(event.status())) {
            order.confirm();
            // Descuenta stock en products-service para cada ítem (síncrono)
            order.getItems().forEach(item ->
                    productCatalog.decreaseStock(item.getProductId(), item.getQuantity()));
            orderRepository.save(order);
            // Publica order.confirmed -> lo consumirá notification-service
            eventPublisher.publishAll(order.pullDomainEvents());
            log.info("Pedido {} confirmado tras pago exitoso", order.getId());
        } else {
            order.markPaymentFailed();
            orderRepository.save(order);
            log.warn("Pedido {} marcado como PAYMENT_FAILED", order.getId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getById(UUID orderId) {
        Order order = orderRepository.findById(OrderId.of(orderId))
                .orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
        return OrderResponse.from(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getByCustomer(UUID customerId) {
        return orderRepository.findByCustomerId(CustomerId.of(customerId)).stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponse cancel(UUID orderId) {
        Order order = orderRepository.findById(OrderId.of(orderId))
                .orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
        order.cancel();
        return OrderResponse.from(orderRepository.save(order));
    }
}
