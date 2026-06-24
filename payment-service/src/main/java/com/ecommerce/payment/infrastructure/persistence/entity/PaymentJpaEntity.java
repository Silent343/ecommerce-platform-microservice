package com.ecommerce.payment.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Modelo de persistencia del pago. Incluye los datos del reembolso y del
 * comprobante de forma plana (embebidos como columnas), ya que Refund y Receipt
 * son piezas pequeñas del agregado.
 */
@Entity
@Table(name = "payments")
public class PaymentJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "order_id", columnDefinition = "uuid", nullable = false, unique = true)
    private UUID orderId;

    @Column(name = "customer_id", columnDefinition = "uuid", nullable = false)
    private UUID customerId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, length = 20)
    private String method;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "receipt_number")
    private String receiptNumber;

    @Column(name = "receipt_issued_at")
    private Instant receiptIssuedAt;

    @Column(name = "refund_id", columnDefinition = "uuid")
    private UUID refundId;

    @Column(name = "refund_reason")
    private String refundReason;

    @Column(name = "refunded_at")
    private Instant refundedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected PaymentJpaEntity() {
    }

    public PaymentJpaEntity(UUID id, UUID orderId, UUID customerId, BigDecimal amount,
                            String currency, String method, String status,
                            String receiptNumber, Instant receiptIssuedAt,
                            UUID refundId, String refundReason, Instant refundedAt,
                            Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
        this.currency = currency;
        this.method = method;
        this.status = status;
        this.receiptNumber = receiptNumber;
        this.receiptIssuedAt = receiptIssuedAt;
        this.refundId = refundId;
        this.refundReason = refundReason;
        this.refundedAt = refundedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getMethod() {
        return method;
    }

    public String getStatus() {
        return status;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public Instant getReceiptIssuedAt() {
        return receiptIssuedAt;
    }

    public UUID getRefundId() {
        return refundId;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public Instant getRefundedAt() {
        return refundedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
