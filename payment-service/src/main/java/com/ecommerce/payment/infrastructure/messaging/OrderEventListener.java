package com.ecommerce.payment.infrastructure.messaging;

import com.ecommerce.payment.application.dto.command.ProcessPaymentCommand;
import com.ecommerce.payment.application.dto.external.OrderCreatedEvent;
import com.ecommerce.payment.application.port.in.ProcessPaymentUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Adaptador de entrada asíncrono: al recibir order.created desde orders-service,
 * dispara automáticamente el cobro. Así el flujo del pedido avanza solo, sin
 * intervención manual.
 *
 * Como el evento no trae método de pago, se usa CARD por defecto (simulación).
 * En una versión con UI de checkout, el cliente elegiría el método y se
 * procesaría vía el endpoint REST.
 */
@Component
public class OrderEventListener {

    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

    private final ProcessPaymentUseCase processPaymentUseCase;

    public OrderEventListener(ProcessPaymentUseCase processPaymentUseCase) {
        this.processPaymentUseCase = processPaymentUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE)
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("Pedido {} recibido para cobro por {} {}",
                event.orderId(), event.currency(), event.totalAmount());

        ProcessPaymentCommand command = new ProcessPaymentCommand(
                event.orderId(),
                event.customerId(),
                event.totalAmount(),
                event.currency(),
                "CARD");

        processPaymentUseCase.process(command);
    }
}
