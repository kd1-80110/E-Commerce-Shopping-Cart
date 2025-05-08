package com.app.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutRequestDto {
    private Long userId; // Optional for guest checkout
    private List<CheckoutItemRequestDto> items;
    private AddressDto shippingAddress;
    private AddressDto billingAddress;
    private String paymentMethod;
}