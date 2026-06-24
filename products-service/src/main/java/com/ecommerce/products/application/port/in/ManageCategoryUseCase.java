package com.ecommerce.products.application.port.in;

import com.ecommerce.products.application.dto.command.CreateCategoryCommand;
import com.ecommerce.products.application.dto.response.CategoryResponse;

import java.util.List;

public interface ManageCategoryUseCase {
    CategoryResponse create(CreateCategoryCommand command);
    List<CategoryResponse> getAll();
}
