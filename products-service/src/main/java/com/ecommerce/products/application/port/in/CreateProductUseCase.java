package com.ecommerce.products.application.port.in;

import com.ecommerce.products.application.dto.command.CreateProductCommand;
import com.ecommerce.products.application.dto.command.UpdateProductCommand;
import com.ecommerce.products.application.dto.response.ProductResponse;

public interface CreateProductUseCase {
    ProductResponse create(CreateProductCommand command);
    ProductResponse update(UpdateProductCommand command);
}
