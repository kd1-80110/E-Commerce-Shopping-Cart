package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {
    private Long productId;
    private String productName;
    private double price;
    private int quantity;
    private String size;
    private String color;
    private String imageUrl;
}