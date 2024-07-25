package org.example.exercice5spring.controller;

import jakarta.validation.Valid;
import org.example.exercice5spring.entity.Furniture;
import org.example.exercice5spring.service.FurnitureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class FurnitureController {
    private final FurnitureService furnitureservice;
    private static final Logger logger = LoggerFactory.getLogger(FurnitureController.class);
    public FurnitureController(FurnitureService furnitureservice) {
        this.furnitureservice = furnitureservice;
    }


    @GetMapping("/furniture")
    public String allFurniture(Model model) {
        List<Furniture> furnitures = furnitureservice.getAllFurnitures();
        model.addAttribute("furnitures",furnitures);
        return "furniture/list";
    }

    @GetMapping("/furniture/add")
    public String addFurniture(Model model) {
        model.addAttribute("furniture", new Furniture());
        return "furniture/add";
    }
    @PostMapping("/furniture/add")
    public String saveFurniture(@Valid @ModelAttribute("furniture") Furniture furniture, BindingResult result) {
        if (result.hasErrors()) {
            return "furniture/add";
        }
        furnitureservice.saveFurniture(furniture);
        return "redirect:/furniture";
    }

    @GetMapping("/furniture/edit/{id}")
    public String editFurniture(@PathVariable Long id, Model model ) {
        Furniture furniture = furnitureservice.getFurnitureById(id);
        if (furniture == null) {
            return "redirect:/furniture";
        }
        model.addAttribute("furniture", furniture);
        return "furniture/add";
    }

    @PostMapping("/furniture/edit")
    public String updateFurniture(@Valid @ModelAttribute("furniture") Furniture furniture, BindingResult result) {
        if (result.hasErrors()) {
            return "furniture/add";
        }
        furnitureservice.saveFurniture(furniture);
        return "redirect:/furniture";
    }

    @GetMapping("/furniture/delete/{id}")
    public String deleteFurniture(@PathVariable Long id) {
        if (id == null) {
            logger.error("The given id must not be null");
            throw new IllegalArgumentException("The given id must not be null");
        }
        logger.info("Deleting furniture with id: " + id);
        furnitureservice.deleteFurnitureById(id);
        return "redirect:/furniture";
    }

}
