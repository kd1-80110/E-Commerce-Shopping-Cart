package com.app.service;

import java.util.List;

import com.app.dto.ProductDto;
import com.app.dto.ProductOptionsDto;

public interface ProductService {

	ProductDto createProduct(ProductDto productDTO);

	ProductDto getProductById(Long id);

	List<ProductDto> getAllProducts();

	ProductDto updateProduct(Long id, ProductDto productDTO);

	void deleteProduct(Long id);

	int getInventory(Long id);

	ProductOptionsDto getProductOptions(Long productId);

	void decreaseQuantity(Long productId, int quantity);

}
