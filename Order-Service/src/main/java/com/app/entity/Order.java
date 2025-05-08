package com.app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private double totalAmount;
    private String currency;

    @Column(columnDefinition = "TEXT")
    private String shippingAddress;

    @Column(columnDefinition = "TEXT")
    private String billingAddress;

    private String paymentMethod;
    private String paymentStatus; // e.g., PAID, PENDING, REFUNDED
    private String transactionId;
    private LocalDateTime orderDate;
    private LocalDateTime shippingDate;
    private String orderStatus; // e.g., PROCESSING, SHIPPED, DELIVERED, CANCELLED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
