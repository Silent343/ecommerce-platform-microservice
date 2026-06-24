package com.ecommerce.products.domain.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String id) {
        super("Categoría no encontrada: " + id);
    }
}
