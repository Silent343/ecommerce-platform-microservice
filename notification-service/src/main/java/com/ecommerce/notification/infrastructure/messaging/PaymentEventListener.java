package com.ecommerce.notification.infrastructure.messaging;

import com.ecommerce.notification.application.dto.external.PaymentCompletedEvent;
import com.ecommerce.notification.application.port.in.HandleEventUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventListener.class);

    private final HandleEventUseCase handleEventUseCase;

    public PaymentEventListener(HandleEventUseCase handleEventUseCase) {
        this.handleEventUseCase = handleEventUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_COMPLETED_QUEUE)
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        log.info("Resultado de pago recibido para notificar: pedido {} estado {}",
                event.orderId(), event.status());
        handleEventUseCase.onPaymentResult(event);
    }
}
