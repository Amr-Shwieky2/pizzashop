package com.example.pizzaapp.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pizzaapp.models.DeliveryAddress;
import com.example.pizzaapp.models.User;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, UUID> {
    List<DeliveryAddress> findByUser(User user);

    long countByUser(User user);
}
