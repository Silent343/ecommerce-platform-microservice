package com.ecommerce.payment.infrastructure.persistence.repository;

import com.ecommerce.payment.domain.model.aggregate.Payment;
import com.ecommerce.payment.domain.model.valueobject.OrderRef;
import com.ecommerce.payment.domain.model.valueobject.PaymentId;
import com.ecommerce.payment.domain.repository.PaymentRepository;
import com.ecommerce.payment.infrastructure.persistence.mapper.PaymentMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository jpaRepository;
    private final PaymentMapper mapper;

    public PaymentRepositoryImpl(PaymentJpaRepository jpaRepository, PaymentMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Payment save(Payment payment) {
        return mapper.toDomain(jpaRepository.save(mapper.toJpa(payment)));
    }

    @Override
    public Optional<Payment> findById(PaymentId id) {
        return jpaRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<Payment> findByOrderRef(OrderRef orderRef) {
        return jpaRepository.findByOrderId(orderRef.value()).map(mapper::toDomain);
    }
}
