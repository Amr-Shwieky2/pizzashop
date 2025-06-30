package com.example.pizzaapp.dtos;

import java.util.List;
import java.util.UUID;

import com.example.pizzaapp.enums.ProductSize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private UUID productId;
    private ProductSize size;
    private List<UUID> toppingIds;
}
