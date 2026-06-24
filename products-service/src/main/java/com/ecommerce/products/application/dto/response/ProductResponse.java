package com.ecommerce.products.application.dto.response;

import com.ecommerce.products.domain.model.aggregate.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        String currency,
        UUID categoryId,
        UUID sellerId,
        int stock,
        boolean active,
        List<ImageResponse> images,
        Instant createdAt
) {
    public static ProductResponse from(Product product) {
        List<ImageResponse> imgs = product.getImages().stream()
                .map(ImageResponse::from)
                .toList();
        return new ProductResponse(
                product.getId().value(),
                product.getSku().value(),
                product.getName(),
                product.getDescription(),
                product.getPrice().amount(),
                product.getPrice().currency(),
                product.getCategoryId() != null ? product.getCategoryId().value() : null,
                product.getSellerId().value(),
                product.getStock().quantity(),
                product.isActive(),
                imgs,
                product.getCreatedAt()
        );
    }
}
