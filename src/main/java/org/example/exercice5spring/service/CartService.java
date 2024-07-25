package org.example.exercice5spring.service;

import org.example.exercice5spring.dao.CartItemRepository;
import org.example.exercice5spring.dao.FurnitureRepository;
import org.example.exercice5spring.entity.CartItem;
import org.example.exercice5spring.entity.Furniture;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private final CartItemRepository repository;
    private final FurnitureService service;

    public CartService(CartItemRepository repository, FurnitureService service) {
        this.repository = repository;
        this.service = service;

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

    public CartItem getCartItemById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
