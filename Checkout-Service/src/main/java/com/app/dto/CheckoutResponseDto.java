package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutResponseDto {
    private Long checkoutId;
//    private String paymentUrl; // URL to redirect for payment
    private String message;
    private double totalAmount;
    private String currency;
}
