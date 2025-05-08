package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.CartItemDto;
import com.app.dto.CartItemResponseDto;
import com.app.dto.SavedItemDto;
import com.app.dto.SavedItemResponseDto;
import com.app.service.CartService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@PostMapping("/add")
	public ResponseEntity<CartItemResponseDto> addItemToCart(@RequestBody CartItemDto cartItemDto,
			HttpServletRequest request) {
		try {
			return ResponseEntity.ok(cartService.addItemToCart(cartItemDto, request));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Or a more specific error response
		}
	}

	@GetMapping("/cart-items")
	public ResponseEntity<List<CartItemResponseDto>> getCart(HttpServletRequest request) {
		return ResponseEntity.ok(cartService.getCartItems(request));
	}

	@PutMapping("/update/{cartItemId}")
	public ResponseEntity<CartItemResponseDto> updateItemQuantity(@PathVariable Long cartItemId,
			@RequestBody int quantity, HttpServletRequest request) {
		return ResponseEntity.ok(cartService.updateCartItemQuantity(cartItemId, quantity, request));
	}

	@DeleteMapping("/remove/{cartItemId}")
	public ResponseEntity<Void> removeItem(@PathVariable Long cartItemId, HttpServletRequest request) {
		cartService.removeCartItem(cartItemId, request);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/save-for-later")
	public ResponseEntity<SavedItemResponseDto> saveForLater(@RequestBody SavedItemDto savedItemDto,
			HttpServletRequest request) {
		return ResponseEntity.ok(cartService.saveItemForLater(savedItemDto, request));
	}

	@GetMapping("/saved-items")
	public ResponseEntity<List<SavedItemResponseDto>> getSavedItems(HttpServletRequest request) {
		return ResponseEntity.ok(cartService.getSavedItems(request));
	}

	@PostMapping("/move-to-cart/{saveItemId}")
	public ResponseEntity<CartItemResponseDto> moveToCart(@PathVariable Long saveItemId, HttpServletRequest request) {
		return ResponseEntity.ok(cartService.moveSavedItemToCart(saveItemId, request));
	}
	
	// Endpoint to clear the cart for a user.
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCartForUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("userId") Long userId) {
        cartService.clearCartForUser(authorizationHeader, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cart-items/{userId}")
    public ResponseEntity<List<CartItemResponseDto>> getCartItemsForUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("userId") Long userId) {
        List<CartItemResponseDto> cartItems = cartService.getCartItemsForUser(authorizationHeader, userId);
        return ResponseEntity.ok(cartItems);
    }
}
