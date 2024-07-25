package org.example.exercice5spring.dao;

import org.example.exercice5spring.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByFurnitureId(Long furnitureId);
}
