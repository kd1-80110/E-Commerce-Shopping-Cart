package com.app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.app.dto.ProductDto;
import com.app.dto.ProductOptionsDto;

@FeignClient(name = "product-service", url = "http://localhost:8082")
public interface ProductServiceProxy {

    @GetMapping("/products/{productId}")
    ProductDto getProductById(@PathVariable Long productId);
    
    @GetMapping("/products/{id}/options") // New endpoint in Product service
    ProductOptionsDto getProductOptions(@PathVariable Long id);
}