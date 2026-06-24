package com.ecommerce.products.infrastructure.persistence.mapper;

import com.ecommerce.products.domain.model.aggregate.Product;
import com.ecommerce.products.domain.model.entity.ProductImage;
import com.ecommerce.products.domain.model.valueobject.CategoryId;
import com.ecommerce.products.domain.model.valueobject.Money;
import com.ecommerce.products.domain.model.valueobject.ProductId;
import com.ecommerce.products.domain.model.valueobject.SellerId;
import com.ecommerce.products.domain.model.valueobject.Sku;
import com.ecommerce.products.domain.model.valueobject.Stock;
import com.ecommerce.products.infrastructure.persistence.entity.ProductImageJpaEntity;
import com.ecommerce.products.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Traduce entre el agregado Product y su modelo de persistencia.
 * Mantiene el dominio libre de anotaciones JPA.
 */
@Component
public class ProductMapper {

    public ProductJpaEntity toJpa(Product product) {
        ProductJpaEntity entity = new ProductJpaEntity(
                product.getId().value(),
                product.getSku().value(),
                product.getName(),
                product.getDescription(),
                product.getPrice().amount(),
                product.getPrice().currency(),
                product.getCategoryId() != null ? product.getCategoryId().value() : null,
                product.getSellerId().value(),
                product.getStock().quantity(),
                product.isActive(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );

        List<ProductImageJpaEntity> images = new ArrayList<>();
        for (ProductImage img : product.getImages()) {
            images.add(new ProductImageJpaEntity(
                    img.getImageId(), img.getUrl(), img.getAltText(), img.isPrimary(), entity));
        }
        entity.setImages(images);
        return entity;
    }

    public Product toDomain(ProductJpaEntity entity) {
        List<ProductImage> images = new ArrayList<>();
        for (ProductImageJpaEntity img : entity.getImages()) {
            images.add(new ProductImage(
                    img.getId(), img.getUrl(), img.getAltText(), img.isPrimary()));
        }

        return Product.reconstitute(
                ProductId.of(entity.getId()),
                Sku.of(entity.getSku()),
                entity.getName(),
                entity.getDescription(),
                Money.of(entity.getPrice(), entity.getCurrency()),
                entity.getCategoryId() != null ? CategoryId.of(entity.getCategoryId()) : null,
                SellerId.of(entity.getSellerId()),
                Stock.of(entity.getStock()),
                images,
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
