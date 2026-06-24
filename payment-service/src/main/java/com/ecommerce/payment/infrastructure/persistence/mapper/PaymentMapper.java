package com.ecommerce.payment.infrastructure.persistence.mapper;

import com.ecommerce.payment.domain.model.aggregate.Payment;
import com.ecommerce.payment.domain.model.entity.Refund;
import com.ecommerce.payment.domain.model.valueobject.CustomerId;
import com.ecommerce.payment.domain.model.valueobject.Money;
import com.ecommerce.payment.domain.model.valueobject.OrderRef;
import com.ecommerce.payment.domain.model.valueobject.PaymentId;
import com.ecommerce.payment.domain.model.valueobject.PaymentMethod;
import com.ecommerce.payment.domain.model.valueobject.PaymentStatus;
import com.ecommerce.payment.domain.model.valueobject.Receipt;
import com.ecommerce.payment.infrastructure.persistence.entity.PaymentJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentJpaEntity toJpa(Payment payment) {
        Receipt receipt = payment.getReceipt();
        Refund refund = payment.getRefund();

        return new PaymentJpaEntity(
                payment.getId().value(),
                payment.getOrderRef().value(),
                payment.getCustomerId().value(),
                payment.getAmount().amount(),
                payment.getAmount().currency(),
                payment.getMethod().name(),
                payment.getStatus().name(),
                receipt != null ? receipt.receiptNumber() : null,
                receipt != null ? receipt.issuedAt() : null,
                refund != null ? refund.getRefundId() : null,
                refund != null ? refund.getReason() : null,
                refund != null ? refund.getRefundedAt() : null,
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }

    public Payment toDomain(PaymentJpaEntity e) {
        Money amount = Money.of(e.getAmount(), e.getCurrency());

        Receipt receipt = null;
        if (e.getReceiptNumber() != null) {
            receipt = Receipt.of(e.getReceiptNumber(), e.getReceiptIssuedAt());
        }

        Refund refund = null;
        if (e.getRefundId() != null) {
            refund = new Refund(e.getRefundId(), amount, e.getRefundReason(), e.getRefundedAt());
        }

        return Payment.reconstitute(
                PaymentId.of(e.getId()),
                OrderRef.of(e.getOrderId()),
                CustomerId.of(e.getCustomerId()),
                amount,
                PaymentMethod.valueOf(e.getMethod()),
                PaymentStatus.valueOf(e.getStatus()),
                receipt,
                refund,
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }
}
