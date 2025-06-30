package com.example.pizzaapp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "order_statuses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatus {
    @Id
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum code;

    public enum OrderStatusEnum {
        PENDING,
        CONFIRMED,
        DELIVERED,
        CANCELLED
    }
}