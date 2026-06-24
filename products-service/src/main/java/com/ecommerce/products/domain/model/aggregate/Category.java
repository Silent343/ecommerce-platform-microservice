package com.ecommerce.products.domain.model.aggregate;

import com.ecommerce.products.domain.model.valueobject.CategoryId;

/**
 * Agregado Category. Pequeño y autónomo: agrupa productos del catálogo.
 * Es un agregado separado de Product (se relacionan por ID, no por composición).
 */
public class Category {

    private final CategoryId id;
    private String name;
    private String description;

    private Category(CategoryId id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static Category create(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }
        return new Category(CategoryId.generate(), name.trim(), description);
    }

    public static Category reconstitute(CategoryId id, String name, String description) {
        return new Category(id, name, description);
    }

    public void rename(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }
        this.name = name.trim();
        this.description = description;
    }

    public CategoryId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
