package com.example.pizzaapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pizzaapp.enums.ProductSize;
import com.example.pizzaapp.models.ProductOption;

import java.util.Optional;
import java.util.UUID;

public interface ProductOptionRepository extends JpaRepository<ProductOption, UUID> {
    Optional<ProductOption> findByProductIdAndSize(UUID productId, ProductSize size);
}
