package com.example.pizzaapp.config;

import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.pizzaapp.models.Role;
import com.example.pizzaapp.models.Role.RoleName;
import com.example.pizzaapp.models.User;
import com.example.pizzaapp.repositories.RoleRepository;
import com.example.pizzaapp.repositories.UserRepository;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Arrays.stream(RoleName.values()).forEach(name -> {
            roleRepository.findByName(name).orElseGet(() -> {
                Role role = new Role(name);
                return roleRepository.save(role);
            });
        });

        userRepository.findByUsername("admin").ifPresentOrElse(
                user -> System.out.println("Admin user already exists."),
                () -> {
                    String encodedPassword = passwordEncoder.encode("ptpt-admin");
                    User admin = new User("admin", encodedPassword, "admin@pizzaapp.com");

                    Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                            .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN not found"));

                    admin.getRoles().add(adminRole);
                    userRepository.save(admin);
                    System.out.println("Admin user created successfully.");
                });
    }
}
