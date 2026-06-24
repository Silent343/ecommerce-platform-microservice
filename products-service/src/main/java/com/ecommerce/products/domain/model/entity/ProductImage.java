package com.ecommerce.products.domain.model.entity;

import java.util.Objects;
import java.util.UUID;

/**
 * Entidad imagen de producto. Vive dentro del agregado Product.
 * Tiene identidad propia pero su ciclo de vida lo gobierna el agregado raíz.
 */
public class ProductImage {

    private final UUID imageId;
    private String url;
    private String altText;
    private boolean primary;

    public ProductImage(UUID imageId, String url, String altText, boolean primary) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("La URL de la imagen no puede estar vacía");
        }
        this.imageId = imageId != null ? imageId : UUID.randomUUID();
        this.url = url;
        this.altText = altText;
        this.primary = primary;
    }

    public static ProductImage create(String url, String altText, boolean primary) {
        return new ProductImage(UUID.randomUUID(), url, altText, primary);
    }

    public void markAsPrimary() {
        this.primary = true;
    }

    public void unmarkPrimary() {
        this.primary = false;
    }

    public UUID getImageId() {
        return imageId;
    }

    public String getUrl() {
        return url;
    }

    public String getAltText() {
        return altText;
    }

    public boolean isPrimary() {
        return primary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductImage that)) return false;
        return imageId.equals(that.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageId);
    }
}
