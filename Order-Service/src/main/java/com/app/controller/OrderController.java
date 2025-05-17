package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.OrderRequestDto;
import com.app.dto.OrderResponseDto;
import com.app.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping
	public ResponseEntity<Long> createOrder(@RequestBody @Valid OrderRequestDto orderRequest) {
		Long orderId = orderService.createOrder(orderRequest);
		return new ResponseEntity<>(orderId, HttpStatus.CREATED);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponseDto> getOrderDetails(@PathVariable Long orderId) {
		return ResponseEntity.ok(orderService.getOrderDetails(orderId));
	}

	@GetMapping("/user")
	public ResponseEntity<List<OrderResponseDto>> getOrderHistory(HttpServletRequest request) {
		return ResponseEntity.ok(orderService.getOrderHistory(request));
	}

	@PutMapping("/{orderId}/cancel")
	public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
		orderService.cancelOrder(orderId);
		return ResponseEntity.noContent().build();
	}
}
