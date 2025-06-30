package com.example.pizzaapp.services;

import com.example.pizzaapp.models.Product;
import com.example.pizzaapp.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Method to find a product by its ID
    public Optional<Product> findById(UUID id) {
        return productRepository.findById(id);
    }
}