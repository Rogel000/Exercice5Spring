package org.example.exercice5spring.service;

import org.example.exercice5spring.dao.CartItemRepository;
import org.example.exercice5spring.entity.CartItem;
import org.example.exercice5spring.entity.Furniture;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {
    private final CartItemRepository repository;
    private final FurnitureService furnitureService;

    public CartService(CartItemRepository repository, FurnitureService furnitureService) {
        this.repository = repository;
        this.furnitureService = furnitureService;
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

    public CartItem prepareCartItemForAddition(Long furnitureId) {
        Furniture furniture = furnitureService.getFurnitureById(furnitureId);
        if (furniture == null) {
            throw new RuntimeException("Le meuble demandé n'existe pas.");
        }

        CartItem existingCartItem = getCartItemByFurnitureId(furnitureId);
        if (existingCartItem != null) {
            return existingCartItem;
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setFurniture(furniture);
            cartItem.setQuantity(1);
            return cartItem;
        }
    }

    @Transactional
    public void saveCartItem(CartItem cartItem) {
        Furniture furniture = furnitureService.getFurnitureById(cartItem.getFurniture().getId());
        if (furniture == null) {
            throw new RuntimeException("Le meuble demandé n'existe pas.");
        }

        int newQuantity = cartItem.getQuantity();
        CartItem existingCartItem = getCartItemByFurnitureId(furniture.getId());

        if (existingCartItem != null) {
            newQuantity += existingCartItem.getQuantity();
        }

        if (furniture.getStock() < newQuantity) {
            throw new RuntimeException("Quantité demandée dépasse le stock disponible.");
        }

        if (existingCartItem != null) {
            existingCartItem.setQuantity(newQuantity);
            saveCart(existingCartItem);
        } else {
            saveCart(cartItem);
        }

        furniture.setStock(furniture.getStock() - cartItem.getQuantity());
        furnitureService.updateFurniture(furniture);
    }

    @Transactional
    public void removeCartItem(Long id) {
        CartItem cartItem = getCartItemById(id);
        if (cartItem != null) {
            Furniture furniture = cartItem.getFurniture();
            if (furniture != null) {
                furniture.setStock(furniture.getStock() + cartItem.getQuantity());
                furnitureService.updateFurniture(furniture);
            }
            deleteCart(id);
        }
    }
}
