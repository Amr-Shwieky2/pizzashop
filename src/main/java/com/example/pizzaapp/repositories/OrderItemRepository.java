package com.example.pizzaapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pizzaapp.models.OrderItem;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}
