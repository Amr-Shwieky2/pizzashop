package com.example.pizzaapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pizzaapp.enums.PaymentMethodEnum;
import com.example.pizzaapp.models.PaymentMethod;
import com.example.pizzaapp.repositories.PaymentMethodRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public PaymentMethod findPaymentMethodByCode(String paymentMethodCode) {
        try {
            PaymentMethodEnum enumValue = PaymentMethodEnum.valueOf(paymentMethodCode);
            return paymentMethodRepository.findByCode(enumValue).orElse(null);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
