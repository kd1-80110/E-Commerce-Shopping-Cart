package com.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.CheckoutItem;

public interface CheckoutItemRepository extends JpaRepository<CheckoutItem, Long> {

	List<CheckoutItem> findByCheckoutId(Long checkoutId);
}
