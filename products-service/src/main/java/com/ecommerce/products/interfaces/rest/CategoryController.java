package com.ecommerce.products.interfaces.rest;

import com.ecommerce.products.application.dto.command.CreateCategoryCommand;
import com.ecommerce.products.application.dto.response.CategoryResponse;
import com.ecommerce.products.application.port.in.ManageCategoryUseCase;
import com.ecommerce.products.interfaces.rest.request.CreateCategoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Categorías del catálogo")
public class CategoryController {

    private final ManageCategoryUseCase manageCategoryUseCase;

    public CategoryController(ManageCategoryUseCase manageCategoryUseCase) {
        this.manageCategoryUseCase = manageCategoryUseCase;
    }

    @GetMapping
    @Operation(summary = "Lista todas las categorías")
    public ResponseEntity<List<CategoryResponse>> list() {
        return ResponseEntity.ok(manageCategoryUseCase.getAll());
    }

    @PostMapping
    @Operation(summary = "Crea una categoría (requiere rol ADMIN)")
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CreateCategoryRequest request) {
        CreateCategoryCommand command = new CreateCategoryCommand(
                request.name(), request.description());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(manageCategoryUseCase.create(command));
    }
}
