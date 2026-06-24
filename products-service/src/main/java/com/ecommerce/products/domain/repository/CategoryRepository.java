package com.ecommerce.products.domain.repository;

import com.ecommerce.products.domain.model.aggregate.Category;
import com.ecommerce.products.domain.model.valueobject.CategoryId;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Category save(Category category);

    Optional<Category> findById(CategoryId id);

    List<Category> findAll();
}
