package com.example.pizzaapp.repositories;

import com.example.pizzaapp.models.Order;
import com.example.pizzaapp.models.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUser(User user);
}
