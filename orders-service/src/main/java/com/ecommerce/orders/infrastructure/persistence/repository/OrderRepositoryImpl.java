package com.ecommerce.orders.infrastructure.persistence.repository;

import com.ecommerce.orders.domain.model.aggregate.Order;
import com.ecommerce.orders.domain.model.valueobject.CustomerId;
import com.ecommerce.orders.domain.model.valueobject.OrderId;
import com.ecommerce.orders.domain.repository.OrderRepository;
import com.ecommerce.orders.infrastructure.persistence.mapper.OrderMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpaRepository;
    private final OrderMapper mapper;

    public OrderRepositoryImpl(OrderJpaRepository jpaRepository, OrderMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Order save(Order order) {
        return mapper.toDomain(jpaRepository.save(mapper.toJpa(order)));
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return jpaRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public List<Order> findByCustomerId(CustomerId customerId) {
        return jpaRepository.findByCustomerId(customerId.value()).stream()
                .map(mapper::toDomain).toList();
    }
}
