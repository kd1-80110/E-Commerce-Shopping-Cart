package com.app.service;

import java.util.List;

import com.app.dto.OrderRequestDto;
import com.app.dto.OrderResponseDto;

import jakarta.servlet.http.HttpServletRequest;

public interface OrderService {

	Long createOrder(OrderRequestDto orderRequest);

	OrderResponseDto getOrderDetails(Long orderId);

//	List<OrderResponseDto> getOrderHistory(Long userId);

	void cancelOrder(Long orderId);

	List<OrderResponseDto> getOrderHistory(HttpServletRequest request);

}
