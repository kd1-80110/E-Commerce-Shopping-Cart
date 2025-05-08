package com.app.dto;

import com.app.entity.Color;
import com.app.entity.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedItemDto {
    private Long productId;
    private Size size;
    private Color color;
}