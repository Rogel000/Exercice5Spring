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
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final FurnitureService furnitureService;

    public CartController(CartService cartService, FurnitureService furnitureService) {
        this.cartService = cartService;
        this.furnitureService = furnitureService;
    }

    @GetMapping
    public String showCart(Model model) {
        List<CartItem> cartItems = cartService.getAllCartItems();
        model.addAttribute("cart", cartItems);
        return "cart/list";
    }

    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, Model model) {
        try {
            CartItem cartItem = cartService.prepareCartItemForAddition(id);
            model.addAttribute("cartItem", cartItem);
            model.addAttribute("furnitures", furnitureService.getAllFurnitures());
            return "cart/add";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/furniture";
        }
    }

    @PostMapping("/add")
    public String saveToCart(@Valid @ModelAttribute CartItem cartItem, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("furnitures", furnitureService.getAllFurnitures());
            return "cart/add";
        }

        try {
            cartService.saveCartItem(cartItem);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("furnitures", furnitureService.getAllFurnitures());
            return "cart/add";
        }

        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {
        cartService.removeCartItem(id);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
}
