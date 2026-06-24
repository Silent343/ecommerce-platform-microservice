package com.ecommerce.payment.domain.repository;

import com.ecommerce.payment.domain.model.aggregate.Payment;
import com.ecommerce.payment.domain.model.valueobject.OrderRef;
import com.ecommerce.payment.domain.model.valueobject.PaymentId;

import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findById(PaymentId id);
    Optional<Payment> findByOrderRef(OrderRef orderRef);
}
