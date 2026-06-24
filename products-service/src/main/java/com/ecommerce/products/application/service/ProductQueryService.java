package com.ecommerce.products.application.service;

import com.ecommerce.products.application.dto.response.ProductResponse;
import com.ecommerce.products.application.port.in.QueryProductUseCase;
import com.ecommerce.products.domain.exception.ProductNotFoundException;
import com.ecommerce.products.domain.model.aggregate.Product;
import com.ecommerce.products.domain.model.valueobject.CategoryId;
import com.ecommerce.products.domain.model.valueobject.ProductId;
import com.ecommerce.products.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Servicio de aplicación para los casos de uso de lectura del catálogo.
 * Separado del de escritura siguiendo la idea de CQRS (Command Query
 * Responsibility Segregation): lecturas y escrituras tienen responsabilidades
 * distintas.
 */
@Service
public class ProductQueryService implements QueryProductUseCase {

    private final ProductRepository productRepository;

    public ProductQueryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getById(UUID productId) {
        Product product = productRepository.findById(ProductId.of(productId))
                .orElseThrow(() -> new ProductNotFoundException(productId.toString()));
        return ProductResponse.from(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getByCategory(UUID categoryId) {
        return productRepository.findByCategory(CategoryId.of(categoryId)).stream()
                .map(ProductResponse::from)
                .toList();
    }
}
