package com.ecommerce.products.application.service;

import com.ecommerce.products.application.dto.command.CreateCategoryCommand;
import com.ecommerce.products.application.dto.response.CategoryResponse;
import com.ecommerce.products.application.port.in.ManageCategoryUseCase;
import com.ecommerce.products.domain.model.aggregate.Category;
import com.ecommerce.products.domain.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService implements ManageCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public CategoryResponse create(CreateCategoryCommand command) {
        Category category = Category.create(command.name(), command.description());
        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .toList();
    }
}
