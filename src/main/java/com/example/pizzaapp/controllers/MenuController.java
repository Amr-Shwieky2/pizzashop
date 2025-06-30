package com.example.pizzaapp.controllers;

import com.example.pizzaapp.dtos.ProductView;
import com.example.pizzaapp.repositories.ProductRepository;
import com.example.pizzaapp.services.ProductViewService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class MenuController {

    private final ProductRepository productRepository;
    private final ProductViewService productViewService;

    public MenuController(ProductRepository productRepository, ProductViewService productViewService) {
        this.productRepository = productRepository;
        this.productViewService = productViewService;
    }

    @GetMapping("/menu")
    public String showMenuPage(Model model) {
        List<ProductView> productViews = productViewService.toProductViews(productRepository.findAllWithDetails());
        model.addAttribute("products", productViews);
        return "pages/menu/index";
    }
}
