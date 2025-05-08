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
public class OrderRequestDto {
    private Long userId;
    private double totalAmount;
    private String currency;
    private AddressDto shippingAddress;
    private AddressDto billingAddress;
    private String paymentMethod;
    private String transactionId;
    private List<OrderItemDto> orderItems;
}