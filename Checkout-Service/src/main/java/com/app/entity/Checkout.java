package com.app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Checkout {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // User associated with the checkout (can be null for guests)

    private double totalAmount;
    private String currency;

    @Column(columnDefinition = "TEXT")
    private String shippingAddress; // JSON or delimited string for simplicity in POC

    @Column(columnDefinition = "TEXT")
    private String billingAddress; // JSON or delimited string for simplicity in POC

    private String paymentMethod; // e.g., UPI, CASH, CREDIT_CARD, NET_BANKING
    private String paymentStatus; // e.g., PENDING, FAILED, SUCCEEDED

    private String transactionId; // Payment gateway transaction ID

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
