package com.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.client.ProductServiceProxy;
import com.app.client.UserServiceProxy;
import com.app.dto.CartItemDto;
import com.app.dto.CartItemResponseDto;
import com.app.dto.ProductDto;
import com.app.dto.ProductOptionsDto;
import com.app.dto.SavedItemDto;
import com.app.dto.SavedItemResponseDto;
import com.app.entity.CartItem;
import com.app.entity.SavedItem;
import com.app.repo.CartItemRepository;
import com.app.repo.SavedItemRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class CartServiceImpl implements CartService {
	
	@Autowired
    private CartItemRepository cartItemRepo;

    @Autowired
    private SavedItemRepository savedItemRepo;

    @Autowired
    private ProductServiceProxy productServiceProxy;

    @Autowired
    private UserServiceProxy userServiceProxy;

    private Long getUserIdFromToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return userServiceProxy.getUserIdFromToken(authorizationHeader);
        }
        throw new RuntimeException("User not authenticated");
    }

    @Override
    public CartItemResponseDto addItemToCart(CartItemDto cartItemDto, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        ProductDto product = productServiceProxy.getProductById(cartItemDto.getProductId());
        ProductOptionsDto productOptions = productServiceProxy.getProductOptions(cartItemDto.getProductId()); // Fetch options

        String requestedSize = cartItemDto.getSize().name();
        String requestedColor = cartItemDto.getColor().name();

        if (productOptions.getSizes() != null && !productOptions.getSizes().contains(requestedSize)) {
            throw new IllegalArgumentException("Invalid size: " + requestedSize + " for product ID: " + cartItemDto.getProductId());
        }

        if (productOptions.getColors() != null && !productOptions.getColors().contains(requestedColor)) {
            throw new IllegalArgumentException("Invalid color: " + requestedColor + " for product ID: " + cartItemDto.getProductId());
        }

        CartItem cartItem = CartItem.builder()
                .userId(userId)
                .productId(cartItemDto.getProductId())
                .quantity(cartItemDto.getQuantity())
                .size(cartItemDto.getSize())
                .color(cartItemDto.getColor())
                .build();

        CartItem savedItem = cartItemRepo.findByUserIdAndProductIdAndSizeAndColor(
                userId, cartItemDto.getProductId(), cartItemDto.getSize(), cartItemDto.getColor())
                .orElse(null);

        if (savedItem != null) {
            savedItem.setQuantity(savedItem.getQuantity() + cartItemDto.getQuantity());
            cartItem = cartItemRepo.save(savedItem);
        } else {
            cartItem = cartItemRepo.save(cartItem);
        }

        return mapCartItemToResponseDto(cartItem, product);
    }

    @Override
    public List<CartItemResponseDto> getCartItems(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        List<CartItem> cartItems = cartItemRepo.findByUserId(userId);
        return cartItems.stream()
                .map(item -> {
                    ProductDto product = productServiceProxy.getProductById(item.getProductId());
                    return mapCartItemToResponseDto(item, product);
                })
                .collect(Collectors.toList());
    }

    @Override
    public CartItemResponseDto updateCartItemQuantity(Long itemId, int quantity, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        CartItem cartItem = cartItemRepo.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cartItem.getUserId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        cartItem.setQuantity(quantity);
        CartItem updatedItem = cartItemRepo.save(cartItem);
        ProductDto product = productServiceProxy.getProductById(updatedItem.getProductId());
        return mapCartItemToResponseDto(updatedItem, product);
    }

    @Override
    public void removeCartItem(Long itemId, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        CartItem cartItem = cartItemRepo.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!cartItem.getUserId().equals(userId)) {
            throw new RuntimeException("Not authorized to delete this cart item");
        }
        cartItemRepo.deleteById(itemId);
    }

    @Override
    public SavedItemResponseDto saveItemForLater(SavedItemDto savedItemDto, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        ProductDto product = productServiceProxy.getProductById(savedItemDto.getProductId());
        ProductOptionsDto productOptions = productServiceProxy.getProductOptions(savedItemDto.getProductId()); // Fetch options

        String requestedSize = savedItemDto.getSize().name();
        String requestedColor = savedItemDto.getColor().name();

        if (productOptions.getSizes() != null && !productOptions.getSizes().contains(requestedSize)) {
            throw new IllegalArgumentException("Invalid size: " + requestedSize + " for product ID: " + savedItemDto.getProductId());
        }

        if (productOptions.getColors() != null && !productOptions.getColors().contains(requestedColor)) {
            throw new IllegalArgumentException("Invalid color: " + requestedColor + " for product ID: " + savedItemDto.getProductId());
        }

        SavedItem savedItem = SavedItem.builder()
                .userId(userId)
                .productId(savedItemDto.getProductId())
                .size(savedItemDto.getSize())
                .color(savedItemDto.getColor())
                .build();
        return mapSavedItemToResponseDto(savedItemRepo.save(savedItem), product);
    }
    
    @Override
    public List<SavedItemResponseDto> getSavedItems(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        List<SavedItem> savedItems = savedItemRepo.findByUserId(userId);
        return savedItems.stream()
                .map(item -> {
                    ProductDto product = productServiceProxy.getProductById(item.getProductId());
                    return mapSavedItemToResponseDto(item, product);
                })
                .collect(Collectors.toList());
    }

    @Override
    public CartItemResponseDto moveSavedItemToCart(Long itemId, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        SavedItem savedItem = savedItemRepo.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Saved item not found"));

        if (!savedItem.getUserId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        ProductDto product = productServiceProxy.getProductById(savedItem.getProductId());

        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setProductId(savedItem.getProductId());
        cartItem.setQuantity(1); // Default quantity when moving to cart
        cartItem.setSize(savedItem.getSize());
        cartItem.setColor(savedItem.getColor());
        CartItem saved = cartItemRepo.save(cartItem);
        savedItemRepo.deleteById(itemId);
        return mapCartItemToResponseDto(saved, product);
    }

    @SuppressWarnings("static-access")
	private CartItemResponseDto mapCartItemToResponseDto(CartItem cartItem, ProductDto product) {
        return new CartItemResponseDto().builder()
                .id(cartItem.getId())
                .productId(cartItem.getProductId())
                .productName(product.getName())
                .price(product.getPrice())
                .quantity(cartItem.getQuantity())
                .size(cartItem.getSize())
                .color(cartItem.getColor())
                .imageUrl(product.getImageUrl())
                .build();
    }


    @SuppressWarnings("static-access")
	private SavedItemResponseDto mapSavedItemToResponseDto(SavedItem savedItem, ProductDto product) {
        return new SavedItemResponseDto().builder()
                .id(savedItem.getId())
                .productId(savedItem.getProductId())
                .productName(product.getName())
                .price(product.getPrice())
                .size(savedItem.getSize())
                .color(savedItem.getColor())
                .imageUrl(product.getImageUrl())
                .build();
    }
    
  //method to clear cart
    @Override
    public void clearCartForUser(String authorizationHeader, Long userId) {
        cartItemRepo.deleteByUserId(userId);
    }

  //method to get cart items for user
    @Override
    public List<CartItemResponseDto> getCartItemsForUser(String authorizationHeader, Long userId) {
        List<CartItem> cartItems = cartItemRepo.findByUserId(userId);
        return cartItems.stream()
                .map(item -> {
                    ProductDto product = productServiceProxy.getProductById(item.getProductId());
                    return CartItemResponseDto.builder() // Build CartItemResponseDto
                            .id(item.getId())  // Assuming CartItem has an ID
                            .productId(item.getProductId())
                            .productName(product.getName())
                            .price(product.getPrice())
                            .quantity(item.getQuantity())
                            .size(item.getSize())
                            .color(item.getColor())
                            .imageUrl(product.getImageUrl())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
