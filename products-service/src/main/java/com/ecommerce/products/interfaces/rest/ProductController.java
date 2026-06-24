package com.ecommerce.products.interfaces.rest;

import com.ecommerce.products.application.dto.command.CreateProductCommand;
import com.ecommerce.products.application.dto.command.UpdateProductCommand;
import com.ecommerce.products.application.dto.command.UpdateStockCommand;
import com.ecommerce.products.application.dto.response.ProductResponse;
import com.ecommerce.products.application.dto.response.StockResponse;
import com.ecommerce.products.application.port.in.CreateProductUseCase;
import com.ecommerce.products.application.port.in.ManageStockUseCase;
import com.ecommerce.products.application.port.in.QueryProductUseCase;
import com.ecommerce.products.interfaces.rest.request.CreateProductRequest;
import com.ecommerce.products.interfaces.rest.request.UpdateProductRequest;
import com.ecommerce.products.interfaces.rest.request.UpdateStockRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Catálogo de productos e inventario")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final QueryProductUseCase queryProductUseCase;
    private final ManageStockUseCase manageStockUseCase;

    public ProductController(CreateProductUseCase createProductUseCase,
                             QueryProductUseCase queryProductUseCase,
                             ManageStockUseCase manageStockUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.queryProductUseCase = queryProductUseCase;
        this.manageStockUseCase = manageStockUseCase;
    }

    @GetMapping
    @Operation(summary = "Lista productos; opcionalmente filtra por categoría")
    public ResponseEntity<List<ProductResponse>> list(
            @RequestParam(required = false) UUID categoryId) {
        List<ProductResponse> products = (categoryId != null)
                ? queryProductUseCase.getByCategory(categoryId)
                : queryProductUseCase.getAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un producto por su ID")
    public ResponseEntity<ProductResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(queryProductUseCase.getById(id));
    }

    @PostMapping
    @Operation(summary = "Crea un producto (requiere rol SELLER o ADMIN)")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
        CreateProductCommand command = new CreateProductCommand(
                request.sku(), request.name(), request.description(), request.price(),
                request.currency(), request.categoryId(), request.sellerId(), request.initialStock());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createProductUseCase.create(command));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza los datos de un producto (requiere rol SELLER o ADMIN)")
    public ResponseEntity<ProductResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProductRequest request) {
        UpdateProductCommand command = new UpdateProductCommand(
                id, request.name(), request.description(), request.price(),
                request.currency(), request.categoryId());
        return ResponseEntity.ok(createProductUseCase.update(command));
    }

    @GetMapping("/{id}/stock")
    @Operation(summary = "Consulta el stock disponible de un producto")
    public ResponseEntity<StockResponse> getStock(@PathVariable UUID id) {
        return ResponseEntity.ok(manageStockUseCase.getStock(id));
    }

    @PutMapping("/{id}/stock")
    @Operation(summary = "Ajusta el stock (operación SET, INCREASE o DECREASE)")
    public ResponseEntity<StockResponse> updateStock(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStockRequest request) {
        UpdateStockCommand command = new UpdateStockCommand(
                id, request.operation(), request.quantity());
        return ResponseEntity.ok(manageStockUseCase.updateStock(command));
    }
}
