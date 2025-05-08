package com.app.service;

import java.util.List;

import com.app.dto.CartItemDto;
import com.app.dto.CheckoutRequestDto;
import com.app.dto.CheckoutResponseDto;
import com.app.dto.PaymentConfirmationDto;

import jakarta.servlet.http.HttpServletRequest;

public interface CheckoutService {

	CheckoutResponseDto proceedToCheckout(CheckoutRequestDto checkoutRequest, HttpServletRequest request);

//	CheckoutResponseDto processPayment(PaymentConfirmationDto paymentConfirmation);

	List<CartItemDto> reviewCart(HttpServletRequest request);

	CheckoutResponseDto guestCheckout(CheckoutRequestDto checkoutRequest, HttpServletRequest request);

	CheckoutResponseDto processPayment(PaymentConfirmationDto paymentConfirmation, HttpServletRequest request);
}
