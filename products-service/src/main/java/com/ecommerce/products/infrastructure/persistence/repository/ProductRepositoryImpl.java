package com.ecommerce.products.infrastructure.persistence.repository;

import com.ecommerce.products.domain.model.aggregate.Product;
import com.ecommerce.products.domain.model.valueobject.CategoryId;
import com.ecommerce.products.domain.model.valueobject.ProductId;
import com.ecommerce.products.domain.model.valueobject.Sku;
import com.ecommerce.products.domain.repository.ProductRepository;
import com.ecommerce.products.infrastructure.persistence.entity.ProductJpaEntity;
import com.ecommerce.products.infrastructure.persistence.mapper.ProductMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia que implementa el puerto ProductRepository
 * delegando en Spring Data JPA y traduciendo con ProductMapper.
 */
@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository jpaRepository;
    private final ProductMapper mapper;

    public ProductRepositoryImpl(ProductJpaRepository jpaRepository, ProductMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = mapper.toJpa(product);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Product> findById(ProductId id) {
        return jpaRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<Product> findBySku(Sku sku) {
        return jpaRepository.findBySku(sku.value()).map(mapper::toDomain);
    }

    @Override
    public boolean existsBySku(Sku sku) {
        return jpaRepository.existsBySku(sku.value());
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Product> findByCategory(CategoryId categoryId) {
        return jpaRepository.findByCategoryId(categoryId.value()).stream()
                .map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(ProductId id) {
        jpaRepository.deleteById(id.value());
    }
}
