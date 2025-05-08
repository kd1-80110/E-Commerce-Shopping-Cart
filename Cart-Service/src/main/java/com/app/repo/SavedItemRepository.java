package com.app.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entity.Color;
import com.app.entity.SavedItem;
import com.app.entity.Size;

@Repository
public interface SavedItemRepository extends JpaRepository<SavedItem, Long> {

	List<SavedItem> findByUserId(Long userId);

	Optional<SavedItem> findByUserIdAndProductIdAndSizeAndColor(Long userId, Long productId, Size size, Color color);

}
