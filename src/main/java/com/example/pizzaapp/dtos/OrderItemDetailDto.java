package com.example.pizzaapp.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDetailDto {
    private String productName;
    private String productCategory;
    private String size;
    private Double basePrice;
    private Double subtotal;
    private List<OrderToppingDto> toppings;
}
