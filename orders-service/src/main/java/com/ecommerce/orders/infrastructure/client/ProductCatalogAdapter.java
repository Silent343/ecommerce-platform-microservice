package com.ecommerce.orders.infrastructure.client;

import com.ecommerce.orders.application.port.out.ProductCatalogPort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Adaptador de salida que implementa ProductCatalogPort hablando con
 * products-service por REST. Usa un RestClient con balanceo de carga (lb://),
 * de modo que la dirección real se resuelve vía Eureka por el nombre lógico
 * del servicio, sin URLs hardcodeadas.
 *
 * Esta es la comunicación SÍNCRONA entre microservicios: orders pregunta a
 * products en tiempo real por precio y stock.
 */
@Component
public class ProductCatalogAdapter implements ProductCatalogPort {

    private final RestClient restClient;

    public ProductCatalogAdapter(RestClient.Builder loadBalancedRestClientBuilder) {
        // PRODUCTS-SERVICE es el nombre con el que el servicio se registra en Eureka
        this.restClient = loadBalancedRestClientBuilder
                .baseUrl("http://PRODUCTS-SERVICE")
                .build();
    }

    @Override
    public ProductInfo getProduct(UUID productId) {
        ProductDto dto = restClient.get()
                .uri("/api/products/{id}", productId)
                .retrieve()
                .body(ProductDto.class);

        if (dto == null) {
            throw new IllegalStateException("Respuesta vacía de products-service para " + productId);
        }
        return new ProductInfo(dto.id(), dto.name(), dto.price(), dto.currency(), dto.stock());
    }

    @Override
    public boolean hasAvailableStock(UUID productId, int quantity) {
        StockDto stock = restClient.get()
                .uri("/api/products/{id}/stock", productId)
                .retrieve()
                .body(StockDto.class);
        return stock != null && stock.quantity() >= quantity;
    }

    @Override
    public void decreaseStock(UUID productId, int quantity) {
        restClient.put()
                .uri("/api/products/{id}/stock", productId)
                .body(new UpdateStockDto("DECREASE", quantity))
                .retrieve()
                .toBodilessEntity();
    }

    // DTOs locales que reflejan las respuestas de products-service
    private record ProductDto(UUID id, String name, BigDecimal price, String currency, int stock) {
    }

    private record StockDto(UUID productId, String sku, int quantity, boolean outOfStock) {
    }

    private record UpdateStockDto(String operation, int quantity) {
    }
}
