package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentConfirmationDto {
    private String checkoutId;
    private String transactionId;
    private String paymentStatus; // SUCCESS, FAILURE
}
