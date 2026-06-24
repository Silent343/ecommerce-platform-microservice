package com.ecommerce.payment.application.service;

import com.ecommerce.payment.application.dto.command.ProcessPaymentCommand;
import com.ecommerce.payment.application.dto.command.RefundCommand;
import com.ecommerce.payment.application.dto.response.PaymentResponse;
import com.ecommerce.payment.application.dto.response.ReceiptResponse;
import com.ecommerce.payment.application.port.in.ProcessPaymentUseCase;
import com.ecommerce.payment.application.port.in.QueryPaymentUseCase;
import com.ecommerce.payment.application.port.in.RefundPaymentUseCase;
import com.ecommerce.payment.application.port.out.DomainEventPublisher;
import com.ecommerce.payment.application.port.out.PaymentGatewayPort;
import com.ecommerce.payment.domain.exception.PaymentNotFoundException;
import com.ecommerce.payment.domain.model.aggregate.Payment;
import com.ecommerce.payment.domain.model.valueobject.CustomerId;
import com.ecommerce.payment.domain.model.valueobject.Money;
import com.ecommerce.payment.domain.model.valueobject.OrderRef;
import com.ecommerce.payment.domain.model.valueobject.PaymentId;
import com.ecommerce.payment.domain.model.valueobject.PaymentMethod;
import com.ecommerce.payment.domain.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Orquesta el cobro. El flujo es:
 *  1. Inicia un Payment en PENDING para el pedido.
 *  2. Llama a la pasarela (PaymentGatewayPort) para cobrar.
 *  3. Según el resultado, marca el pago COMPLETED o FAILED (lógica en el agregado).
 *  4. Publica el evento payment.completed que orders-service consume para
 *     confirmar o rechazar el pedido.
 */
@Service
public class PaymentService implements ProcessPaymentUseCase, QueryPaymentUseCase, RefundPaymentUseCase {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final PaymentGatewayPort paymentGateway;
    private final DomainEventPublisher eventPublisher;

    public PaymentService(PaymentRepository paymentRepository,
                          PaymentGatewayPort paymentGateway,
                          DomainEventPublisher eventPublisher) {
        this.paymentRepository = paymentRepository;
        this.paymentGateway = paymentGateway;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public PaymentResponse process(ProcessPaymentCommand command) {
        Money amount = Money.of(command.amount(),
                command.currency() != null ? command.currency() : "PEN");
        PaymentMethod method = command.method() != null
                ? PaymentMethod.valueOf(command.method()) : PaymentMethod.CARD;

        Payment payment = Payment.initiate(
                OrderRef.of(command.orderId()), CustomerId.of(command.customerId()), amount, method);

        // Cobro a través de la pasarela (simulada)
        PaymentGatewayPort.ChargeResult result = paymentGateway.charge(amount, method);

        if (result.approved()) {
            payment.markCompleted();
            log.info("Pago {} COMPLETED para pedido {}", payment.getId(), command.orderId());
        } else {
            payment.markFailed();
            log.warn("Pago {} FAILED para pedido {}: {}",
                    payment.getId(), command.orderId(), result.message());
        }

        Payment saved = paymentRepository.save(payment);
        // Publica payment.completed -> orders-service confirma o rechaza el pedido
        eventPublisher.publishAll(payment.pullDomainEvents());

        return PaymentResponse.from(saved);
    }

    @Override
    @Transactional
    public PaymentResponse refund(RefundCommand command) {
        Payment payment = paymentRepository.findById(PaymentId.of(command.paymentId()))
                .orElseThrow(() -> new PaymentNotFoundException(command.paymentId().toString()));
        payment.refund(command.reason());
        return PaymentResponse.from(paymentRepository.save(payment));
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getById(UUID paymentId) {
        return PaymentResponse.from(findById(paymentId));
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getByOrder(UUID orderId) {
        Payment payment = paymentRepository.findByOrderRef(OrderRef.of(orderId))
                .orElseThrow(() -> new PaymentNotFoundException("para el pedido " + orderId));
        return PaymentResponse.from(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public ReceiptResponse getReceipt(UUID paymentId) {
        return ReceiptResponse.from(findById(paymentId));
    }

    private Payment findById(UUID paymentId) {
        return paymentRepository.findById(PaymentId.of(paymentId))
                .orElseThrow(() -> new PaymentNotFoundException(paymentId.toString()));
    }
}
