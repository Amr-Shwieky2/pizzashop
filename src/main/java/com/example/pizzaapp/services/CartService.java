package com.example.pizzaapp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pizzaapp.dtos.CartItemDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CartService {

    private final ObjectMapper objectMapper;

    public CartService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<CartItemDto> parseCartItems(String cartJson) throws Exception {
        return objectMapper.readValue(cartJson, new TypeReference<List<CartItemDto>>() {
        });
    }

    public boolean isCartEmpty(List<CartItemDto> cartItems) {
        return cartItems == null || cartItems.isEmpty();
    }
}
