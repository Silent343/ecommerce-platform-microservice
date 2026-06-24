package com.ecommerce.products.domain.repository;

import com.ecommerce.products.domain.model.aggregate.Product;
import com.ecommerce.products.domain.model.valueobject.CategoryId;
import com.ecommerce.products.domain.model.valueobject.ProductId;
import com.ecommerce.products.domain.model.valueobject.Sku;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para la persistencia de productos. Definido en términos del
 * dominio, sin conocer JPA. La infraestructura provee la implementación (DIP).
 */
public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(ProductId id);

    Optional<Product> findBySku(Sku sku);

    boolean existsBySku(Sku sku);

    List<Product> findAll();

    List<Product> findByCategory(CategoryId categoryId);

    void deleteById(ProductId id);
}
