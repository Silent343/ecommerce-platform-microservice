package com.ecommerce.payment.domain.model.aggregate;

import com.ecommerce.payment.domain.exception.PaymentAlreadyProcessedException;
import com.ecommerce.payment.domain.exception.RefundNotAllowedException;
import com.ecommerce.payment.domain.model.entity.Refund;
import com.ecommerce.payment.domain.model.event.PaymentCompletedEvent;
import com.ecommerce.payment.domain.model.valueobject.CustomerId;
import com.ecommerce.payment.domain.model.valueobject.Money;
import com.ecommerce.payment.domain.model.valueobject.OrderRef;
import com.ecommerce.payment.domain.model.valueobject.PaymentId;
import com.ecommerce.payment.domain.model.valueobject.PaymentMethod;
import com.ecommerce.payment.domain.model.valueobject.PaymentStatus;
import com.ecommerce.payment.domain.model.valueobject.Receipt;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Agregado raíz del pago. Modela el ciclo:
 *
 *   PENDING --markCompleted()--> COMPLETED --refund()--> REFUNDED
 *   PENDING --markFailed()-----> FAILED
 *
 * Al completarse o fallar, acumula un PaymentCompletedEvent que la capa de
 * aplicación publicará para que orders-service confirme o rechace el pedido.
 */
public class Payment {

    private final PaymentId id;
    private final OrderRef orderRef;
    private final CustomerId customerId;
    private final Money amount;
    private final PaymentMethod method;
    private PaymentStatus status;
    private Receipt receipt;
    private Refund refund;
    private final Instant createdAt;
    private Instant updatedAt;

    private final transient List<Object> domainEvents = new ArrayList<>();

    private Payment(PaymentId id, OrderRef orderRef, CustomerId customerId, Money amount,
                    PaymentMethod method, PaymentStatus status, Receipt receipt, Refund refund,
                    Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.orderRef = orderRef;
        this.customerId = customerId;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.receipt = receipt;
        this.refund = refund;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /** Inicia un pago en estado PENDING para un pedido. */
    public static Payment initiate(OrderRef orderRef, CustomerId customerId,
                                   Money amount, PaymentMethod method) {
        Instant now = Instant.now();
        return new Payment(PaymentId.generate(), orderRef, customerId, amount,
                method, PaymentStatus.PENDING, null, null, now, now);
    }

    public static Payment reconstitute(PaymentId id, OrderRef orderRef, CustomerId customerId,
                                       Money amount, PaymentMethod method, PaymentStatus status,
                                       Receipt receipt, Refund refund,
                                       Instant createdAt, Instant updatedAt) {
        return new Payment(id, orderRef, customerId, amount, method, status,
                receipt, refund, createdAt, updatedAt);
    }

    /** Marca el pago como completado, emite el comprobante y el evento de éxito. */
    public void markCompleted() {
        requirePending();
        this.status = PaymentStatus.COMPLETED;
        this.receipt = Receipt.issue(this.id);
        this.touch();
        registerEvent(new PaymentCompletedEvent(
                orderRef.value(), id.value(), "COMPLETED", Instant.now()));
    }

    /** Marca el pago como fallido y emite el evento correspondiente. */
    public void markFailed() {
        requirePending();
        this.status = PaymentStatus.FAILED;
        this.touch();
        registerEvent(new PaymentCompletedEvent(
                orderRef.value(), id.value(), "FAILED", Instant.now()));
    }

    public void refund(String reason) {
        if (this.status != PaymentStatus.COMPLETED) {
            throw new RefundNotAllowedException(
                    "solo se puede reembolsar un pago COMPLETED (estado actual: " + status + ")");
        }
        this.refund = Refund.create(this.amount, reason);
        this.status = PaymentStatus.REFUNDED;
        this.touch();
    }

    private void requirePending() {
        if (this.status != PaymentStatus.PENDING) {
            throw new PaymentAlreadyProcessedException(id.toString());
        }
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    private void registerEvent(Object event) {
        this.domainEvents.add(event);
    }

    public List<Object> pullDomainEvents() {
        List<Object> copy = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return copy;
    }

    public PaymentId getId() {
        return id;
    }

    public OrderRef getOrderRef() {
        return orderRef;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getAmount() {
        return amount;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public Refund getRefund() {
        return refund;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
