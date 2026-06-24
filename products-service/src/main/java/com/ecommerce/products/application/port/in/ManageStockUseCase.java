package com.ecommerce.products.application.port.in;

import com.ecommerce.products.application.dto.command.UpdateStockCommand;
import com.ecommerce.products.application.dto.response.StockResponse;

import java.util.UUID;

public interface ManageStockUseCase {
    StockResponse getStock(UUID productId);
    StockResponse updateStock(UpdateStockCommand command);
}
