package com.app.dto;

import java.util.List;

import com.app.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private double price;
//    private Size size;
//    private Color color;
    private int quantity;
    private Category category;
    private String imageUrl;
    private List<String> availableSizes; // Add this
    private List<String> availableColors; // Add this
}
