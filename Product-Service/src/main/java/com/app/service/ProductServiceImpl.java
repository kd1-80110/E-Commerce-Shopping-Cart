package com.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.ProductDto;
import com.app.dto.ProductOptionsDto;
import com.app.entity.Product;
import com.app.mapper.ProductMapper;
import com.app.repo.ProductRepo;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepo productRepository;

	@Override
	public ProductDto createProduct(ProductDto productDTO) {
		Product product = ProductMapper.toEntity(productDTO);
		return ProductMapper.toDTO(productRepository.save(product));
	}

	@Override
	public ProductDto getProductById(Long id) {
		Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
		return ProductMapper.toDTO(product);
	}

	@Override
	public List<ProductDto> getAllProducts() {
		return productRepository.findAll().stream().map(ProductMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public ProductDto updateProduct(Long id, ProductDto productDTO) {
		Product existing = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
		existing.setName(productDTO.getName());
		existing.setDescription(productDTO.getDescription());
		existing.setPrice(productDTO.getPrice());
		existing.setQuantity(productDTO.getQuantity());
		existing.setCategory(productDTO.getCategory());
		existing.setAvailableColors(productDTO.getAvailableColors());
		existing.setAvailableSizes(productDTO.getAvailableSizes());
		existing.setImageUrl(productDTO.getImageUrl());
		return ProductMapper.toDTO(productRepository.save(existing));
	}

	@Override
	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}

	@Override
	public int getInventory(Long id) {
		return productRepository.findById(id).map(Product::getQuantity)
				.orElseThrow(() -> new RuntimeException("Product not found"));
	}

	@Override
	public ProductOptionsDto getProductOptions(Long productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found"));

		ProductOptionsDto optionsDto = new ProductOptionsDto();
		optionsDto.setSizes(product.getAvailableSizes());
		optionsDto.setColors(product.getAvailableColors());
		return optionsDto;
	}
	
	// Added method to decrease product quantity
    @Override
    @Transactional
    public void decreaseQuantity(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        int newQuantity = product.getQuantity() - quantity;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Insufficient stock for product ID: " + productId);
        }
        product.setQuantity(newQuantity);
        productRepository.save(product);
    }
	
}
