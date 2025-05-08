package com.app.dto;

import com.app.entity.Color;
import com.app.entity.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedItemResponseDto {
    private Long id;
    private Long productId;
    private String productName;
    private double price;
    private Size size;
    private Color color;
    private String imageUrl;
}
