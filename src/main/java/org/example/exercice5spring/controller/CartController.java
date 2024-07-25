package org.example.exercice5spring.controller;

import jakarta.validation.Valid;
import org.example.exercice5spring.entity.CartItem;
import org.example.exercice5spring.entity.Furniture;
import org.example.exercice5spring.service.CartService;
import org.example.exercice5spring.service.FurnitureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CartController {
    private final CartService cartService;
    private final FurnitureService furnitureService;

    public CartController(CartService cartService, FurnitureService furnitureService) {
        this.cartService = cartService;
        this.furnitureService = furnitureService;
    }

    @GetMapping("/cart")
    public String showCart(Model model) {
        List<CartItem> cartItems = cartService.getAllCartItems();
        for (CartItem item : cartItems) {
            if (item.getFurniture() == null) {
                System.out.println("CartItem with ID " + item.getId() + " has no associated Furniture.");
            }
        }
        model.addAttribute("cart", cartItems);
        return "cart/list";
    }


    @GetMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Long id, Model model) {
        Furniture furniture = furnitureService.getFurnitureById(id);
        if (furniture == null) {
            model.addAttribute("errorMessage", "Le meuble demandé n'existe pas.");
            return "redirect:/furniture";
        }
        CartItem cartItem = new CartItem();
        cartItem.setFurniture(furniture);
        cartItem.setQuantity(1); // Initial quantity

        model.addAttribute("cartItem", cartItem);
        model.addAttribute("furnitures", furnitureService.getAllFurnitures()); // Pass the list of furnitures
        return "cart/add";
    }





    @PostMapping("/cart/add")
    public String saveToCart(@Valid @ModelAttribute CartItem cartItem, BindingResult result, Model model) {
        Furniture furniture = furnitureService.getFurnitureById(cartItem.getFurniture().getId());
        if (furniture == null) {
            model.addAttribute("errorMessage", "Le meuble demandé n'existe pas.");
            model.addAttribute("furnitures", furnitureService.getAllFurnitures());
            return "cart/add";
        }

        if (result.hasErrors()) {
            model.addAttribute("furnitures", furnitureService.getAllFurnitures());
            return "cart/add";
        }

        if (furniture.getStock() < cartItem.getQuantity()) {
            model.addAttribute("errorMessage", "Quantité demandée dépasse le stock disponible.");
            return "cart/add";
        }

        cartService.saveCart(cartItem);
        furniture.setStock(furniture.getStock() - cartItem.getQuantity());
        furnitureService.updateFurniture(furniture);
        return "redirect:/cart";
    }


    @GetMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {
        CartItem cartItem = cartService.getCartItemById(id);
        if (cartItem != null) {
            Furniture furniture = cartItem.getFurniture();
            if (furniture != null) {
                furniture.setStock(furniture.getStock() + cartItem.getQuantity());
                furnitureService.updateFurniture(furniture);
            }
            cartService.deleteCart(id);
        }
        return "redirect:/cart";
    }



    @GetMapping("/cart/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
}
