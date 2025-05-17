package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.ProductDto;
import com.app.dto.ProductOptionsDto;
import com.app.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping
	public ResponseEntity<ProductDto> create(@RequestBody @Valid ProductDto dto) {
		return ResponseEntity.ok(productService.createProduct(dto));
	}

	@GetMapping
	public ResponseEntity<List<ProductDto>> getAll() {
		return ResponseEntity.ok(productService.getAllProducts());
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<ProductDto>> getAlll() {
		return ResponseEntity.ok(productService.getAllProducts());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
		return ResponseEntity.ok(productService.getProductById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody ProductDto dto) {
		return ResponseEntity.ok(productService.updateProduct(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/inventory")
	public ResponseEntity<Integer> getInventory(@PathVariable Long id) {
		return ResponseEntity.ok(productService.getInventory(id));
	}

	@GetMapping("/{id}/options")
	public ResponseEntity<ProductOptionsDto> getProductOptions(@PathVariable Long id) {
		ProductOptionsDto options = productService.getProductOptions(id);
		return ResponseEntity.ok(options);
	}
	
	// Added method to decrease product quantity
    @PostMapping("/decreaseQuantity/{productId}")
    public ResponseEntity<Void> decreaseQuantity(
            @PathVariable Long productId,
            @RequestParam("quantity") int quantity) {
        productService.decreaseQuantity(productId, quantity);
        return ResponseEntity.noContent().build();
    }
	
}
