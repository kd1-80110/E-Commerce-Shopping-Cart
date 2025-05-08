package com.app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.app.dto.OrderRequestDto;

@FeignClient(name = "order-service", url = "http://localhost:8085")
public interface OrderServiceProxy {
    @PostMapping("/orders")
    Long createOrder(@RequestBody OrderRequestDto orderRequestDto);
}
