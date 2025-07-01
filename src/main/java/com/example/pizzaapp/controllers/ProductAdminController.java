package com.example.pizzaapp.controllers;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.pizzaapp.models.Product;
import com.example.pizzaapp.repositories.ProductRepository;

@Controller
@RequestMapping("/admin/products")
@PreAuthorize("hasRole('ADMIN')")
public class ProductAdminController {

    private final ProductRepository productRepository;

    public ProductAdminController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productRepository.findAllWithDetails());
        return "admin/products";
    }

    @PostMapping("/{id}/updateStock")
    public String updateStock(@PathVariable UUID id, @RequestParam boolean inStock) {
        productRepository.findById(id).ifPresent(p -> {
            p.setInStock(inStock);
            productRepository.save(p);
        });
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable UUID id) {
        productRepository.deleteById(id);
        return "redirect:/admin/products";
    }
}
