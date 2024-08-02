package org.example.exercice5spring.controller;

import jakarta.validation.Valid;
import org.example.exercice5spring.entity.Furniture;
import org.example.exercice5spring.service.FurnitureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/furniture")
public class FurnitureController {
    private final FurnitureService furnitureService;
    private static final Logger logger = LoggerFactory.getLogger(FurnitureController.class);

    public FurnitureController(FurnitureService furnitureService) {
        this.furnitureService = furnitureService;
    }

    @GetMapping
    public String allFurniture(Model model) {
        List<Furniture> furnitures = furnitureService.getAllFurnitures();
        model.addAttribute("furnitures", furnitures);
        return "furniture/list";
    }

    @GetMapping("/add")
    public String addFurniture(Model model) {
        model.addAttribute("furniture", new Furniture());
        return "furniture/add";
    }

    @PostMapping("/add")
    public String saveFurniture(@Valid @ModelAttribute("furniture") Furniture furniture, BindingResult result) {
        if (result.hasErrors()) {
            return "furniture/add";
        }
        furnitureService.saveFurniture(furniture);
        return "redirect:/furniture";
    }

    @GetMapping("/edit/{id}")
    public String editFurniture(@PathVariable Long id, Model model) {
        try {
            Furniture furniture = furnitureService.getFurnitureById(id);
            model.addAttribute("furniture", furniture);
            return "furniture/add";
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            return "redirect:/furniture";
        }
    }

    @PostMapping("/edit")
    public String updateFurniture(@Valid @ModelAttribute("furniture") Furniture furniture, BindingResult result) {
        if (result.hasErrors()) {
            return "furniture/add";
        }
        furnitureService.saveFurniture(furniture);
        return "redirect:/furniture";
    }

    @GetMapping("/delete/{id}")
    public String deleteFurniture(@PathVariable Long id) {
        try {
            furnitureService.deleteFurnitureById(id);
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
        }
        return "redirect:/furniture";
    }
}
