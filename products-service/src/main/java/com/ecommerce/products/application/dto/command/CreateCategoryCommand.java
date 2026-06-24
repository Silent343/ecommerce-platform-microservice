package com.ecommerce.products.application.dto.command;

public record CreateCategoryCommand(
        String name,
        String description
) {
}
