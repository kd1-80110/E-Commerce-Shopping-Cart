package com.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;
	private Long productId;
	
	@Min(value = 0, message = "Quantity must be zero or more")
	private int quantity;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private Size size;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private Color color;
}
