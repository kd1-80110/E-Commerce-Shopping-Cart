package com.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUserId(Long userId);

}
