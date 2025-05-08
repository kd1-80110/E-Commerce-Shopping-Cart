package com.app.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private double totalAmount;
    private String currency;
    private AddressDto shippingAddress;
    private AddressDto billingAddress;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionId;
    private LocalDateTime orderDate;
    private LocalDateTime shippingDate;
    private String orderStatus;
    private List<OrderItemDto> orderItems;
}
