package com.ecommerce.notification.infrastructure.messaging;

import com.ecommerce.notification.application.dto.external.OrderConfirmedEvent;
import com.ecommerce.notification.application.port.in.HandleEventUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

    private final HandleEventUseCase handleEventUseCase;

    public OrderEventListener(HandleEventUseCase handleEventUseCase) {
        this.handleEventUseCase = handleEventUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_CONFIRMED_QUEUE)
    public void onOrderConfirmed(OrderConfirmedEvent event) {
        log.info("Pedido confirmado recibido para notificar: {}", event.orderId());
        handleEventUseCase.onOrderConfirmed(event);
    }
}
