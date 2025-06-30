package com.example.pizzaapp.repositories;

import com.example.pizzaapp.enums.PaymentMethodEnum;
import com.example.pizzaapp.models.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, PaymentMethodEnum> {

    Optional<PaymentMethod> findByCode(PaymentMethodEnum code);

    boolean existsByCode(PaymentMethodEnum code);
}
