package com.ecommerce.products.application.service;

import com.ecommerce.products.application.dto.command.CreateProductCommand;
import com.ecommerce.products.application.dto.command.UpdateProductCommand;
import com.ecommerce.products.application.dto.command.UpdateStockCommand;
import com.ecommerce.products.application.dto.response.ProductResponse;
import com.ecommerce.products.application.dto.response.StockResponse;
import com.ecommerce.products.application.port.in.CreateProductUseCase;
import com.ecommerce.products.application.port.in.DomainEventPublisher;
import com.ecommerce.products.application.port.in.ManageStockUseCase;
import com.ecommerce.products.domain.exception.DuplicateSkuException;
import com.ecommerce.products.domain.exception.ProductNotFoundException;
import com.ecommerce.products.domain.model.aggregate.Product;
import com.ecommerce.products.domain.model.valueobject.CategoryId;
import com.ecommerce.products.domain.model.valueobject.Money;
import com.ecommerce.products.domain.model.valueobject.ProductId;
import com.ecommerce.products.domain.model.valueobject.SellerId;
import com.ecommerce.products.domain.model.valueobject.Sku;
import com.ecommerce.products.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Servicio de aplicación para los casos de uso de escritura del catálogo.
 * Orquesta el dominio: valida precondiciones, invoca el agregado, persiste
 * y publica los eventos que el agregado haya acumulado.
 */
@Service
public class ProductCommandService implements CreateProductUseCase, ManageStockUseCase {

    private final ProductRepository productRepository;
    private final DomainEventPublisher eventPublisher;

    public ProductCommandService(ProductRepository productRepository,
                                 DomainEventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public ProductResponse create(CreateProductCommand command) {
        Sku sku = Sku.of(command.sku());
        if (productRepository.existsBySku(sku)) {
            throw new DuplicateSkuException(sku.value());
        }

        Money price = Money.of(command.price(),
                command.currency() != null ? command.currency() : "PEN");
        CategoryId categoryId = command.categoryId() != null
                ? CategoryId.of(command.categoryId()) : null;
        SellerId sellerId = SellerId.of(command.sellerId());

        Product product = Product.create(sku, command.name(), command.description(),
                price, categoryId, sellerId, command.initialStock());

        Product saved = productRepository.save(product);
        eventPublisher.publishAll(product.pullDomainEvents());

        return ProductResponse.from(saved);
    }

    @Override
    @Transactional
    public ProductResponse update(UpdateProductCommand command) {
        Product product = productRepository.findById(ProductId.of(command.productId()))
                .orElseThrow(() -> new ProductNotFoundException(command.productId().toString()));

        Money price = Money.of(command.price(),
                command.currency() != null ? command.currency() : "PEN");
        CategoryId categoryId = command.categoryId() != null
                ? CategoryId.of(command.categoryId()) : null;

        product.updateDetails(command.name(), command.description(), price, categoryId);
        return ProductResponse.from(productRepository.save(product));
    }

    @Override
    @Transactional(readOnly = true)
    public StockResponse getStock(UUID productId) {
        Product product = productRepository.findById(ProductId.of(productId))
                .orElseThrow(() -> new ProductNotFoundException(productId.toString()));
        return toStockResponse(product);
    }

    @Override
    @Transactional
    public StockResponse updateStock(UpdateStockCommand command) {
        Product product = productRepository.findById(ProductId.of(command.productId()))
                .orElseThrow(() -> new ProductNotFoundException(command.productId().toString()));

        switch (command.operation().toUpperCase()) {
            case "SET" -> product.setStock(command.quantity());
            case "INCREASE" -> product.increaseStock(command.quantity());
            case "DECREASE" -> product.decreaseStock(command.quantity());
            default -> throw new IllegalArgumentException(
                    "Operación de stock inválida: " + command.operation() + " (usar SET, INCREASE o DECREASE)");
        }

        Product saved = productRepository.save(product);
        eventPublisher.publishAll(product.pullDomainEvents());
        return toStockResponse(saved);
    }

    private StockResponse toStockResponse(Product product) {
        return new StockResponse(
                product.getId().value(),
                product.getSku().value(),
                product.getStock().quantity(),
                product.getStock().isOutOfStock());
    }
}
