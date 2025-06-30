package com.example.pizzaapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pizzaapp.models.OrderStatus;
import com.example.pizzaapp.models.OrderStatus.OrderStatusEnum;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, OrderStatusEnum> {
    Optional<OrderStatus> findByCode(OrderStatusEnum code);

    boolean existsByCode(OrderStatusEnum code);
}
