package com.example.pizzaapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pizzaapp.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
