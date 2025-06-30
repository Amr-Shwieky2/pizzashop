package com.example.pizzaapp.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto {
    private UUID id;
    private LocalDateTime createdAt;
    private String status;
    private String paymentMethod;
    private Double totalPrice;
    private String deliveryAddress;
    private String customerName;
    private String customerEmail;
    private List<OrderItemDetailDto> items;
}
