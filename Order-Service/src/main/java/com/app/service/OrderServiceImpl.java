package com.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.AddressDto;
import com.app.dto.OrderItemDto;
import com.app.dto.OrderRequestDto;
import com.app.dto.OrderResponseDto;
import com.app.entity.Order;
import com.app.entity.OrderItem;
import com.app.repo.OrderItemRepository;
import com.app.repo.OrderRepository;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public Long createOrder(OrderRequestDto orderRequest) {
    	LocalDateTime orderDate = LocalDateTime.now();
    	LocalDateTime shippingDate = calculateShippingDate(orderDate); // Implement this method
        Order order = Order.builder()
                .userId(orderRequest.getUserId())
                .totalAmount(orderRequest.getTotalAmount())
                .currency(orderRequest.getCurrency())
                .shippingAddress(convertAddressDtoToString(orderRequest.getShippingAddress()))
                .billingAddress(convertAddressDtoToString(orderRequest.getBillingAddress()))
                .paymentMethod(orderRequest.getPaymentMethod())
                .paymentStatus("PAID") // Assuming payment is handled by Checkout service
                .transactionId(orderRequest.getTransactionId())
                .orderDate(orderDate)
                .shippingDate(shippingDate)
                .orderStatus("PROCESSING")
                .build();

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = orderRequest.getOrderItems().stream()
                .map(itemDto -> OrderItem.builder()
                        .order(savedOrder)
                        .productId(itemDto.getProductId())
                        .productName(itemDto.getProductName())
                        .price(itemDto.getPrice())
                        .quantity(itemDto.getQuantity())
                        .size(itemDto.getSize())
                        .color(itemDto.getColor())
                        .imageUrl(itemDto.getImageUrl())
                        .build())
                .collect(Collectors.toList());
        orderItemRepository.saveAll(orderItems);

        return savedOrder.getId();
    }

    @Override
    public OrderResponseDto getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        return convertToOrderResponseDto(order, orderItems);
    }

    @Override
    public List<OrderResponseDto> getOrderHistory(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(order -> {
                    List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
                    return convertToOrderResponseDto(order, orderItems);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
        // Implement logic to check if order can be cancelled based on its status
        order.setOrderStatus("CANCELLED");
        orderRepository.save(order);
        // Implement any necessary inventory adjustments or refund processes
    }

    private LocalDateTime calculateShippingDate(LocalDateTime orderDate) {
    	// Implement your shipping date calculation logic here
    	// This is a placeholder, replace with your actual logic
    	return orderDate.plusDays(3); // Example: 3 days from order date
    	}
    
    private OrderResponseDto convertToOrderResponseDto(Order order, List<OrderItem> orderItems) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .currency(order.getCurrency())
                .shippingAddress(convertStringtoAddressDto(order.getShippingAddress()))
                .billingAddress(convertStringtoAddressDto(order.getBillingAddress()))
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .transactionId(order.getTransactionId())
                .orderDate(order.getOrderDate())
                .shippingDate(order.getShippingDate())
                .orderStatus(order.getOrderStatus())
                .orderItems(orderItems.stream()
                        .map(item -> OrderItemDto.builder()
                                .productId(item.getProductId())
                                .productName(item.getProductName())
                                .price(item.getPrice())
                                .quantity(item.getQuantity())
                                .size(item.getSize())
                                .color(item.getColor())
                                .imageUrl(item.getImageUrl())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private String convertAddressDtoToString(AddressDto addressDto) {
        return addressDto != null ? addressDto.toString() : null;
    }

    private AddressDto convertStringtoAddressDto(String addressString) {
        // Implement proper JSON or delimited string parsing in real app
        return AddressDto.builder().street(addressString).build(); // Simple placeholder
    }
}