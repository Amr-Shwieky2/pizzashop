package com.example.pizzaapp.controllers;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.pizzaapp.models.Topping;
import com.example.pizzaapp.repositories.ToppingRepository;

@Controller
@RequestMapping("/admin/ingredients")
@PreAuthorize("hasRole('ADMIN')")
public class IngredientAdminController {

    private final ToppingRepository toppingRepository;

    public IngredientAdminController(ToppingRepository toppingRepository) {
        this.toppingRepository = toppingRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("ingredients", toppingRepository.findAll());
        return "admin/ingredients";
    }

    @PostMapping("/{id}/updatePrice")
    public String updatePrice(@PathVariable UUID id, @RequestParam double price) {
        toppingRepository.findById(id).ifPresent(t -> {
            t.setPrice(price);
            toppingRepository.save(t);
        });
        return "redirect:/admin/ingredients";
    }

    @PostMapping("/{id}/updateStock")
    public String updateStock(@PathVariable UUID id, @RequestParam boolean inStock) {
        toppingRepository.findById(id).ifPresent(t -> {
            t.setInStock(inStock);
            toppingRepository.save(t);
        });
        return "redirect:/admin/ingredients";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable UUID id) {
        toppingRepository.deleteById(id);
        return "redirect:/admin/ingredients";
    }

    @PostMapping("/add")
    public String add(@RequestParam String name,
                      @RequestParam double price,
                      @RequestParam(required = false) String imageUrl) {
        Topping topping = new Topping(name, price, imageUrl);
        toppingRepository.save(topping);
        return "redirect:/admin/ingredients";
    }
}
