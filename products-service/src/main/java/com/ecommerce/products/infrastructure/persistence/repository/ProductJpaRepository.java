package com.ecommerce.products.infrastructure.persistence.repository;

import com.ecommerce.products.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID> {
    Optional<ProductJpaEntity> findBySku(String sku);
    boolean existsBySku(String sku);
    List<ProductJpaEntity> findByCategoryId(UUID categoryId);
}
