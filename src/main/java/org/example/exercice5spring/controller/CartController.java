package org.example.exercice5spring.controller;

import jakarta.validation.Valid;
import org.example.exercice5spring.entity.CartItem;
import org.example.exercice5spring.entity.Furniture;
import org.example.exercice5spring.service.CartService;
import org.example.exercice5spring.service.FurnitureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
        System.out.println("GET request to /cart/add");
        Furniture furniture = furnitureService.getFurnitureById(id);
        if (furniture == null) {
            model.addAttribute("errorMessage", "Le meuble demandé n'existe pas.");
            model.addAttribute("furniture", furnitureService.getAllFurnitures());
            return "cart/add";
        }
        CartItem cartItem = new CartItem();
        cartItem.setFurniture(furniture);
        model.addAttribute("cart", cartItem);
        model.addAttribute("furniture", furnitureService.getAllFurnitures());
        return "cart/add";
    }


    @PostMapping("/cart/add")
    public String saveToCart(@Valid @ModelAttribute("cart") CartItem cartItem, BindingResult result, Model model) {
        System.out.println("POST request to /cart/add");
        if (result.hasErrors()) {
            model.addAttribute("furniture", furnitureService.getAllFurnitures());
            return "cart/add";
        }
        cartService.saveCart(cartItem);
        return "redirect:/cart";
    }

    @GetMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return "redirect:/cart";
    }

    @GetMapping("/cart/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
}
