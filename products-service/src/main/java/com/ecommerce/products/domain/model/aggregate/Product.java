package com.ecommerce.products.domain.model.aggregate;

import com.ecommerce.products.domain.model.entity.ProductImage;
import com.ecommerce.products.domain.model.event.ProductCreatedEvent;
import com.ecommerce.products.domain.model.event.StockUpdatedEvent;
import com.ecommerce.products.domain.model.valueobject.CategoryId;
import com.ecommerce.products.domain.model.valueobject.Money;
import com.ecommerce.products.domain.model.valueobject.ProductId;
import com.ecommerce.products.domain.model.valueobject.SellerId;
import com.ecommerce.products.domain.model.valueobject.Sku;
import com.ecommerce.products.domain.model.valueobject.Stock;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Agregado raíz del catálogo. Encapsula producto, su precio, su inventario
 * y sus imágenes. Es la única puerta de entrada para modificarlos y protege
 * los invariantes del negocio (stock no negativo, una sola imagen principal, etc.).
 *
 * El vendedor (sellerId) y la categoría (categoryId) se referencian por ID:
 * son entidades de otros agregados/servicios, no se embeben aquí.
 */
public class Product {

    private final ProductId id;
    private final Sku sku;
    private String name;
    private String description;
    private Money price;
    private CategoryId categoryId;
    private final SellerId sellerId;
    private Stock stock;
    private final List<ProductImage> images;
    private boolean active;
    private final Instant createdAt;
    private Instant updatedAt;

    private final transient List<Object> domainEvents = new ArrayList<>();

    private Product(ProductId id, Sku sku, String name, String description, Money price,
                    CategoryId categoryId, SellerId sellerId, Stock stock,
                    List<ProductImage> images, boolean active,
                    Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.sellerId = sellerId;
        this.stock = stock;
        this.images = images != null ? images : new ArrayList<>();
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /** Factory para crear un producto nuevo. Emite ProductCreatedEvent. */
    public static Product create(Sku sku, String name, String description, Money price,
                                 CategoryId categoryId, SellerId sellerId, int initialStock) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        ProductId newId = ProductId.generate();
        Instant now = Instant.now();
        Product product = new Product(newId, sku, name, description, price, categoryId,
                sellerId, Stock.of(initialStock), new ArrayList<>(), true, now, now);
        product.registerEvent(new ProductCreatedEvent(
                newId.value(), sku.value(), name, price.amount(), sellerId.value(), now));
        return product;
    }

    /** Reconstituye un producto existente desde persistencia (sin eventos). */
    public static Product reconstitute(ProductId id, Sku sku, String name, String description,
                                       Money price, CategoryId categoryId, SellerId sellerId,
                                       Stock stock, List<ProductImage> images, boolean active,
                                       Instant createdAt, Instant updatedAt) {
        return new Product(id, sku, name, description, price, categoryId, sellerId,
                stock, images, active, createdAt, updatedAt);
    }

    public void updateDetails(String name, String description, Money price, CategoryId categoryId) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.touch();
    }

    public void increaseStock(int amount) {
        int previous = this.stock.quantity();
        this.stock = this.stock.increase(amount);
        this.touch();
        registerEvent(new StockUpdatedEvent(
                id.value(), sku.value(), previous, this.stock.quantity(), Instant.now()));
    }

    public void decreaseStock(int amount) {
        int previous = this.stock.quantity();
        this.stock = this.stock.decrease(amount);
        this.touch();
        registerEvent(new StockUpdatedEvent(
                id.value(), sku.value(), previous, this.stock.quantity(), Instant.now()));
    }

    public void setStock(int quantity) {
        int previous = this.stock.quantity();
        this.stock = Stock.of(quantity);
        this.touch();
        registerEvent(new StockUpdatedEvent(
                id.value(), sku.value(), previous, quantity, Instant.now()));
    }

    public void addImage(ProductImage image) {
        if (image.isPrimary()) {
            images.forEach(ProductImage::unmarkPrimary);
        } else if (images.isEmpty()) {
            image.markAsPrimary();
        }
        this.images.add(image);
        this.touch();
    }

    public void activate() {
        this.active = true;
        this.touch();
    }

    public void deactivate() {
        this.active = false;
        this.touch();
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    private void registerEvent(Object event) {
        this.domainEvents.add(event);
    }

    public List<Object> pullDomainEvents() {
        List<Object> copy = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return copy;
    }

    public ProductId getId() {
        return id;
    }

    public Sku getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Money getPrice() {
        return price;
    }

    public CategoryId getCategoryId() {
        return categoryId;
    }

    public SellerId getSellerId() {
        return sellerId;
    }

    public Stock getStock() {
        return stock;
    }

    public List<ProductImage> getImages() {
        return Collections.unmodifiableList(images);
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
