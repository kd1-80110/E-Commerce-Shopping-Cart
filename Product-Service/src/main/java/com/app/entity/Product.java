package com.app.entity;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Product name is mandatory")
	private String name;

	private String description;

	@PositiveOrZero(message = "Price must be zero or positive")
	private double price;

	// Inventory/stock quantity
	@Min(value = 0, message = "Quantity must be zero or more")
	private int quantity;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private Category category;

	// URL or path to the product image
	private String imageUrl;
	
	// Store available sizes as a collection of Enum strings
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "product_available_sizes", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "size", nullable = false)
    private List<String> availableSizes;

    // Store available colors as a collection of Enum strings
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "product_available_colors", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "color", nullable = false)
    private List<String> availableColors;
}
