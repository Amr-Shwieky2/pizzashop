package com.example.pizzaapp.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDto {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
