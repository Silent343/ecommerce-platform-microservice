package com.ecommerce.orders.infrastructure.messaging;

import com.ecommerce.orders.application.dto.external.PaymentCompletedEvent;
import com.ecommerce.orders.application.port.in.HandlePaymentResultUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Adaptador de entrada asíncrono: escucha la cola payment.completed.orders.queue
 * y delega en el caso de uso que confirma o marca como fallido el pedido.
 *
 * Es el otro lado de la moneda del controller REST: aquí la "petición" no llega
 * por HTTP sino por un mensaje de RabbitMQ publicado por payment-service.
 */
@Component
public class PaymentEventListener {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventListener.class);

    private final HandlePaymentResultUseCase handlePaymentResultUseCase;

    public PaymentEventListener(HandlePaymentResultUseCase handlePaymentResultUseCase) {
        this.handlePaymentResultUseCase = handlePaymentResultUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_COMPLETED_QUEUE)
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        log.info("Evento de pago recibido para el pedido {} con estado {}",
                event.orderId(), event.status());
        handlePaymentResultUseCase.handle(event);
    }
}
