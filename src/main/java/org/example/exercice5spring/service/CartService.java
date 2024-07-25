package org.example.exercice5spring.service;

import org.example.exercice5spring.dao.CartItemRepository;
import org.example.exercice5spring.entity.CartItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private final CartItemRepository repository;

    public CartService(CartItemRepository repository) {
        this.repository = repository;

    }

    public void saveCart(CartItem item) {
        repository.save(item);
    }

    public List<CartItem> getAllCartItems() {
        return repository.findAll();
    }

    public void clearCart() {
        repository.deleteAll();
    }

    public void deleteCart(Long id) {
        repository.deleteById(id);
    }

    public CartItem getCartItemById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public CartItem getCartItemByFurnitureId(Long furnitureId) {
        return repository.findByFurnitureId(furnitureId);
    }
}
