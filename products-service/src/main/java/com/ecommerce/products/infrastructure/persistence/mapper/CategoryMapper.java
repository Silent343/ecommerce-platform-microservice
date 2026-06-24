package com.ecommerce.products.infrastructure.persistence.mapper;

import com.ecommerce.products.domain.model.aggregate.Category;
import com.ecommerce.products.domain.model.valueobject.CategoryId;
import com.ecommerce.products.infrastructure.persistence.entity.CategoryJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryJpaEntity toJpa(Category category) {
        return new CategoryJpaEntity(
                category.getId().value(), category.getName(), category.getDescription());
    }

    public Category toDomain(CategoryJpaEntity entity) {
        return Category.reconstitute(
                CategoryId.of(entity.getId()), entity.getName(), entity.getDescription());
    }
}
