package com.ecommerce.orders.infrastructure.persistence.repository;

import com.ecommerce.orders.domain.model.aggregate.Cart;
import com.ecommerce.orders.domain.model.valueobject.CustomerId;
import com.ecommerce.orders.domain.repository.CartRepository;
import com.ecommerce.orders.infrastructure.persistence.mapper.CartMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CartRepositoryImpl implements CartRepository {

    private final CartJpaRepository jpaRepository;
    private final CartMapper mapper;

    public CartRepositoryImpl(CartJpaRepository jpaRepository, CartMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Cart save(Cart cart) {
        return mapper.toDomain(jpaRepository.save(mapper.toJpa(cart)));
    }

    @Override
    public Optional<Cart> findByCustomerId(CustomerId customerId) {
        return jpaRepository.findByCustomerId(customerId.value()).map(mapper::toDomain);
    }
}
