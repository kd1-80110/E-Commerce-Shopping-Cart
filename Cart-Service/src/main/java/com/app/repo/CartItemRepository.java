package com.app.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entity.CartItem;
import com.app.entity.Color;
import com.app.entity.Size;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findByUserId(Long userId);

	Optional<CartItem> findByUserIdAndProductIdAndSizeAndColor(Long userId, Long productId, Size size, Color color);

	void deleteByUserId(Long userId);

}
