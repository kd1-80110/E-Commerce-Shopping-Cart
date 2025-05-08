package com.app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserServiceProxy {
	
	@GetMapping("/users/user-id") // Assuming an endpoint to get user ID from token
    Long getUserIdFromToken(@RequestHeader("Authorization") String authorizationHeader);
}

