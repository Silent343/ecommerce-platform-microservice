package com.ecommerce.orders.application.port.out;

import java.util.UUID;

/**
 * Puerto de salida para consultar y actualizar el catálogo (products-service).
 * La implementación concreta usa REST sobre Eureka. El dominio/aplicación no
 * saben CÓMO se llama al otro servicio, solo QUÉ necesitan de él.
 */
public interface ProductCatalogPort {

    /** Datos mínimos del producto que orders necesita para armar el pedido. */
    record ProductInfo(UUID productId, String name, java.math.BigDecimal price,
                       String currency, int availableStock) {
    }

    ProductInfo getProduct(UUID productId);

    boolean hasAvailableStock(UUID productId, int quantity);

    void decreaseStock(UUID productId, int quantity);
}
