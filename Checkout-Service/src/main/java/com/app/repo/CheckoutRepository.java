package com.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.Checkout;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {

}
