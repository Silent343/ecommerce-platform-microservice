package com.ecommerce.products.application.dto.response;

import com.ecommerce.products.domain.model.entity.ProductImage;

import java.util.UUID;

public record ImageResponse(
        UUID imageId,
        String url,
        String altText,
        boolean primary
) {
    public static ImageResponse from(ProductImage image) {
        return new ImageResponse(
                image.getImageId(), image.getUrl(), image.getAltText(), image.isPrimary());
    }
}
