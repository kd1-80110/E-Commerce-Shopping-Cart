package com.app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.dto.ProductDto;

@FeignClient(name = "product-service", url = "http://localhost:8082")
public interface ProductServiceProxy {
    @GetMapping("/products/{productId}")
    ProductDto getProductById(@PathVariable Long productId);
    
    @PostMapping("/products/decreaseQuantity/{productId}")
    void decreaseProductQuantity(
            @PathVariable Long productId,
            @RequestParam("quantity") int quantity);
}