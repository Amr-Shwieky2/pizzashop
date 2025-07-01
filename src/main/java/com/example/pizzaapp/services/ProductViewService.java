package com.example.pizzaapp.services;

import com.example.pizzaapp.dtos.ProductView;
import com.example.pizzaapp.models.Product;
import com.example.pizzaapp.models.Topping;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductViewService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<ProductView> toProductViews(List<Product> products) {
        return products.stream()
                .map(this::toProductView)
                .toList();
    }

    public ProductView toProductView(Product product) {
        return new ProductView(
                product,
                formatSizesJson(product),
                formatToppingsJson(product),
                product.getDefaultSize(),
                getDefaultPrice(product));
    }

    private String formatSizesJson(Product product) {
        try {
            Map<String, Map<String, Object>> uniqueOptionsBySize = new LinkedHashMap<>();

            for (var opt : product.getOptions()) {
                uniqueOptionsBySize.putIfAbsent(
                        opt.getSize().name(),
                        Map.of(
                                "label", opt.getSize().name(),
                                "price", opt.getPrice(),
                                "id", opt.getId()));
            }

            return objectMapper.writeValueAsString(uniqueOptionsBySize.values());
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }

    private String formatToppingsJson(Product product) {
        try {
            return objectMapper.writeValueAsString(product.getAllowedToppings().stream()
                    .filter(Topping::isInStock)
                    .map(t -> Map.of(
                            "label", t.getName(),
                            "price", t.getPrice(),
                            "id", t.getId(),
                            "imageUrl", t.getImageUrl()))
                    .toList());
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }

    private double getDefaultPrice(Product product) {
        return product.getOptions().stream()
                .filter(opt -> opt.getSize() == product.getDefaultSize())
                .findFirst()
                .map(opt -> opt.getPrice())
                .orElse(0.0);
    }

}
