package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.app.client")
public class CheckoutServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CheckoutServiceApplication.class, args);
	}

}
