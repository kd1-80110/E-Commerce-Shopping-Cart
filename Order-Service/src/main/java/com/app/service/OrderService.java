package com.app.service;

import java.util.List;

import com.app.dto.OrderRequestDto;
import com.app.dto.OrderResponseDto;

public interface OrderService {

	Long createOrder(OrderRequestDto orderRequest);

	OrderResponseDto getOrderDetails(Long orderId);

	List<OrderResponseDto> getOrderHistory(Long userId);

	void cancelOrder(Long orderId);

}
