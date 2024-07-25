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

    public CartItem saveCart(CartItem item) {
        return repository.save(item);
    }
    public List<CartItem> getAllCartItems(){
        return repository.findAll();
    }
    public void clearCart(){
        repository.deleteAll();
    }
    public void deleteCart(Long id){
        repository.deleteById(id);
    }
}
