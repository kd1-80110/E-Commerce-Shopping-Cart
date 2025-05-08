package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutItemRequestDto {
    private Long productId;
    private int quantity;
    private String size;
    private String color;
}