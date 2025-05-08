package com.app.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.app.dto.CartItemDto;

@FeignClient(name = "cart-service", url = "http://localhost:8083")
public interface CartServiceProxy {
    @GetMapping("/cart/cart-items")
    List<CartItemDto> getCartItems(@RequestHeader("Authorization") String authorizationHeader);

 // Added method to clear cart for a specific user
    @DeleteMapping("/cart/clear/{userId}")
    void clearCartForUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("userId") Long userId);

    @GetMapping("/cart/cart-items/{userId}")
    List<CartItemDto> getCartItemsForUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("userId") Long userId);
}