package com.app.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.app.dto.CartItemDto;
import com.app.dto.CartItemResponseDto;
import com.app.dto.SavedItemDto;
import com.app.dto.SavedItemResponseDto;

import jakarta.servlet.http.HttpServletRequest;

public interface CartService {

	CartItemResponseDto moveSavedItemToCart(Long itemId, HttpServletRequest request);

	List<SavedItemResponseDto> getSavedItems(HttpServletRequest request);

	SavedItemResponseDto saveItemForLater(SavedItemDto savedItemDto, HttpServletRequest request);

	void removeCartItem(Long itemId, HttpServletRequest request);

	CartItemResponseDto updateCartItemQuantity(Long itemId, int quantity, HttpServletRequest request);

	List<CartItemResponseDto> getCartItems(HttpServletRequest request);

	CartItemResponseDto addItemToCart(CartItemDto cartItemDto, HttpServletRequest request);

	void clearCartForUser(String authorizationHeader, Long userId);

//	List<CartItemDto> getCartItemsForUser(String authorizationHeader, Long userId);
	
	List<CartItemResponseDto> getCartItemsForUser(String authorizationHeader, Long userId);

	void removeSavedItem(Long savedItemId, HttpServletRequest request);
	
}
