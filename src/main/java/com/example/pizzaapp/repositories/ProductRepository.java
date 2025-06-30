package com.example.pizzaapp.repositories;

import com.example.pizzaapp.models.Product;
import com.example.pizzaapp.models.ProductCategory;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByCategory(ProductCategory category);

    @EntityGraph(attributePaths = { "category", "options", "allowedToppings" })
    @Query("SELECT p FROM Product p")
    List<Product> findAllWithDetails();
}
