package com.example.pizzaapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pizzaapp.models.Topping;

import java.util.UUID;

public interface ToppingRepository extends JpaRepository<Topping, UUID> {
}
