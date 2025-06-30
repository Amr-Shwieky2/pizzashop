package com.example.pizzaapp.models;

import com.example.pizzaapp.enums.PaymentMethodEnum;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "payment_methods")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "code", nullable = false, unique = true)
    private PaymentMethodEnum code;
}
