package com.example.pizzaapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pizzaapp.models.Role;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    // Define a method to find a Role by its name
    Optional<Role> findByName(Role.RoleName name);
}