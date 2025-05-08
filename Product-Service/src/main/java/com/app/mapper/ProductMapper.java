package com.app.mapper;

import com.app.dto.ProductDto;
import com.app.entity.Product;

public class ProductMapper {

	public static ProductDto toDTO(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .category(product.getCategory())
                .imageUrl(product.getImageUrl())
                .availableSizes(product.getAvailableSizes()) // Add this
                .availableColors(product.getAvailableColors()) // Add this
                .build();
    }

    public static Product toEntity(ProductDto dto) {
        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .category(dto.getCategory())
                .imageUrl(dto.getImageUrl())
                .availableSizes(dto.getAvailableSizes()) // Add this
                .availableColors(dto.getAvailableColors()) // Add this
                .build();
    }
}
