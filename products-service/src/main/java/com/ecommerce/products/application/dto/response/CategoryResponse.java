package com.ecommerce.products.application.dto.response;

import com.ecommerce.products.domain.model.aggregate.Category;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        String description
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId().value(), category.getName(), category.getDescription());
    }
}
