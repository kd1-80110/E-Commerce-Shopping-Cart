package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.CartItemDto;
import com.app.dto.CheckoutRequestDto;
import com.app.dto.CheckoutResponseDto;
import com.app.dto.PaymentConfirmationDto;
import com.app.service.CheckoutService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/checkout")
@CrossOrigin
public class CheckoutController {

	@Autowired
	private CheckoutService checkoutService;

	@PostMapping
	public ResponseEntity<CheckoutResponseDto> proceedToCheckout(@RequestBody @Valid CheckoutRequestDto checkoutRequest,
			HttpServletRequest request) {
		return ResponseEntity.ok(checkoutService.proceedToCheckout(checkoutRequest, request));
	}

	@GetMapping("/review")
	public ResponseEntity<List<CartItemDto>> reviewCart(HttpServletRequest request) {
		return ResponseEntity.ok(checkoutService.reviewCart(request));
	}

	@PostMapping("/payment")
	public ResponseEntity<CheckoutResponseDto> processPayment(
			@RequestBody @Valid PaymentConfirmationDto paymentConfirmation, HttpServletRequest request) {
		return ResponseEntity.ok(checkoutService.processPayment(paymentConfirmation,request));
	}

	@PostMapping("/guest")
	public ResponseEntity<CheckoutResponseDto> guestCheckout(@RequestBody @Valid CheckoutRequestDto checkoutRequest,
			HttpServletRequest request) {
		return ResponseEntity.ok(checkoutService.guestCheckout(checkoutRequest, request));
	}
}