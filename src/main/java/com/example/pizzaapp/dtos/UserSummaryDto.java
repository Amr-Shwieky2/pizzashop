package com.example.pizzaapp.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSummaryDto {
    private UUID id;
    private String username;
    private String role;
}
