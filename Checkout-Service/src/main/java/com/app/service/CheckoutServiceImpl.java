package com.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.client.CartServiceProxy;
import com.app.client.OrderServiceProxy;
import com.app.client.ProductServiceProxy;
import com.app.client.UserServiceProxy;
import com.app.dto.AddressDto;
import com.app.dto.CartItemDto;
import com.app.dto.CheckoutRequestDto;
import com.app.dto.CheckoutResponseDto;
import com.app.dto.OrderItemDto;
import com.app.dto.OrderRequestDto;
import com.app.dto.PaymentConfirmationDto;
import com.app.dto.ProductDto;
import com.app.entity.Checkout;
import com.app.entity.CheckoutItem;
import com.app.repo.CheckoutItemRepository;
import com.app.repo.CheckoutRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class CheckoutServiceImpl implements CheckoutService {

	@Autowired
	private CheckoutRepository checkoutRepository;

	@Autowired
	private CheckoutItemRepository checkoutItemRepository;

	@Autowired
	private CartServiceProxy cartServiceProxy;

	@Autowired
	private ProductServiceProxy productServiceProxy;

	@Autowired
	private OrderServiceProxy orderServiceProxy;

	@Autowired
	private UserServiceProxy userServiceProxy;

	// Placeholder for user ID retrieval
	private Long getUserIdFromToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			return userServiceProxy.getUserIdFromToken(authorizationHeader);
		}
		throw new RuntimeException("User not authenticated");
	}

	@Override
	public CheckoutResponseDto proceedToCheckout(CheckoutRequestDto checkoutRequest, HttpServletRequest request) {
		Long userId = getUserIdFromToken(request);
		String authorizationHeader = request.getHeader("Authorization"); // Get the header
		List<CartItemDto> cartItems = cartServiceProxy.getCartItems(authorizationHeader); // Pass only the header

		if (cartItems.isEmpty()) {
			throw new IllegalStateException("Cart is empty.");
		}

		double totalAmount = cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
		String currency = "INR"; // Default currency

		Checkout checkout = Checkout.builder().userId(userId).totalAmount(totalAmount).currency(currency)
				.shippingAddress(convertAddressDtoToString(checkoutRequest.getShippingAddress()))
				.billingAddress(convertAddressDtoToString(checkoutRequest.getBillingAddress()))
				.paymentMethod(checkoutRequest.getPaymentMethod()).paymentStatus("PENDING").build();

		Checkout savedCheckout = checkoutRepository.save(checkout);

		List<CheckoutItem> checkoutItems = cartItems.stream()
				.map(cartItem -> CheckoutItem.builder().checkout(savedCheckout).productId(cartItem.getProductId())
						.productName(cartItem.getProductName()).price(cartItem.getPrice())
						.quantity(cartItem.getQuantity()).size(cartItem.getSize()).color(cartItem.getColor())
						.imageUrl(cartItem.getImageUrl()).build())
				.collect(Collectors.toList());
		checkoutItemRepository.saveAll(checkoutItems);

		// In a real app, you'd integrate with a payment gateway here
		// and return a payment URL or initiation details
		return CheckoutResponseDto.builder().checkoutId(savedCheckout.getId()).totalAmount(totalAmount)
				.currency(currency).message("Checkout initiated. Proceed to payment.")
//                .paymentUrl("/payment/" + savedCheckout.getId()) // Placeholder
				.build();
	}

	@Override
	public CheckoutResponseDto processPayment(PaymentConfirmationDto paymentConfirmation, HttpServletRequest request) {
		Checkout checkout = checkoutRepository.findById(Long.parseLong(paymentConfirmation.getCheckoutId()))
				.orElseThrow(() -> new IllegalArgumentException("Invalid checkout ID."));

		checkout.setPaymentStatus(paymentConfirmation.getPaymentStatus());
		checkout.setTransactionId(paymentConfirmation.getTransactionId());
		checkoutRepository.save(checkout);

		if ("SUCCESS".equalsIgnoreCase(paymentConfirmation.getPaymentStatus())) {
			String authorizationHeader = request.getHeader("Authorization");
			clearCartAndAdjustInventory(checkout, authorizationHeader); // Pass the authorization header
			createOrder(checkout, authorizationHeader); // Pass the authorization header
			return CheckoutResponseDto.builder().checkoutId(checkout.getId())
					.message("Payment successful. Order created.").totalAmount(checkout.getTotalAmount())
					.currency(checkout.getCurrency()).build();
		} else {
			throw new RuntimeException("Payment failed.");
		}
	}

	private void createOrder(Checkout checkout, String authorizationHeader) { // Add authorizationHeader parameter
		List<CheckoutItem> checkoutItems = checkoutItemRepository.findByCheckoutId(checkout.getId());
		List<OrderItemDto> orderItems = checkoutItems.stream()
				.map(item -> {
					try {
						ProductDto product = productServiceProxy.getProductById(item.getProductId()); // Get product details
						return OrderItemDto.builder()
								.productId(item.getProductId())
								.productName(product.getName()) // Use product name from ProductDto
								.price(item.getPrice())
								.quantity(item.getQuantity())
								.size(item.getSize())
								.color(item.getColor())
								.imageUrl(product.getImageUrl()) // Use image URL from ProductDto
								.build();
					} catch (Exception e) {
						// Handle the exception (e.g., log it, return null, or throw a custom exception)
						System.err.println("Error fetching product details for product ID " + item.getProductId() + ": " + e.getMessage());
						return null; // Or handle as appropriate for your application
					}
				})
				.filter(orderItem -> orderItem != null) //remove null orderItems from the list
				.collect(Collectors.toList());

		OrderRequestDto orderRequest = OrderRequestDto.builder().userId(checkout.getUserId())
				.totalAmount(checkout.getTotalAmount()).currency(checkout.getCurrency())
				.shippingAddress(convertStringtoAddressDto(checkout.getShippingAddress()))
				.billingAddress(convertStringtoAddressDto(checkout.getBillingAddress()))
				.paymentMethod(checkout.getPaymentMethod()).transactionId(checkout.getTransactionId())
				.orderItems(orderItems).build();

		orderServiceProxy.createOrder(orderRequest);
		// Optionally clear the cart after successful order
		// cartServiceProxy.clearCart(null);
	}

	private void clearCartAndAdjustInventory(Checkout checkout, String authorizationHeader) { // Add authorizationHeader
		// get this from the original request.
		Long userId = checkout.getUserId();

		List<CartItemDto> cartItems = cartServiceProxy.getCartItemsForUser(authorizationHeader, userId);
		if (cartItems != null) { // Check if cartItems is not null
			for (CartItemDto cartItem : cartItems) {
				// Update product quantity. Handle exceptions appropriately.
				try {
					productServiceProxy.decreaseProductQuantity(cartItem.getProductId(), cartItem.getQuantity());
				} catch (Exception e) {
					// Log the error and decide how to handle it (e.g., compensate later, notify admin)
					System.err.println("Error updating product quantity for product ID " + cartItem.getProductId() + ": "
							+ e.getMessage());
					// Consider a rollback scenario.
					throw new RuntimeException("Failed to update product quantity. Order might be inconsistent.", e);
				}
			}
			cartServiceProxy.clearCartForUser(authorizationHeader, userId); // Use the authorizationHeader
		}
	}

	@Override
	public List<CartItemDto> reviewCart(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		return cartServiceProxy.getCartItems(authorizationHeader);
	}

	@Override
	public CheckoutResponseDto guestCheckout(CheckoutRequestDto checkoutRequest, HttpServletRequest request) {
		// Logic similar to proceedToCheckout, but userId will be null
		checkoutRequest.setUserId(null);
		return proceedToCheckout(checkoutRequest, request);
	}

	// Helper methods for address conversion (simple JSON for POC)
	private String convertAddressDtoToString(AddressDto addressDto) {
		// Use Jackson or Gson for proper JSON conversion in real app
		return addressDto != null ? addressDto.toString() : null;
	}

	private AddressDto convertStringtoAddressDto(String addressString) {
		// Use Jackson or Gson for proper JSON conversion in real app
		return AddressDto.builder().street(addressString).build(); // Simple placeholder
	}
}