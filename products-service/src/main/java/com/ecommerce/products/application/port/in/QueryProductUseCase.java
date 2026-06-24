package com.ecommerce.products.application.port.in;

import com.ecommerce.products.application.dto.response.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface QueryProductUseCase {
    ProductResponse getById(UUID productId);
    List<ProductResponse> getAll();
    List<ProductResponse> getByCategory(UUID categoryId);
}
