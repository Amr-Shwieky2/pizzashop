package com.example.pizzaapp.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryDto {
    private UUID id;
    private LocalDateTime createdAt;
    private String status;
    private String paymentMethod;
    private Double totalPrice;
    private int itemCount;
    private String deliveryAddress;
}
