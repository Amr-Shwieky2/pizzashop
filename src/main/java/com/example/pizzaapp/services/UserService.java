package com.example.pizzaapp.services;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pizzaapp.dtos.UserSummaryDto;
import com.example.pizzaapp.models.Role;
import com.example.pizzaapp.models.User;
import com.example.pizzaapp.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserByPrincipal(Principal principal) {
        if (principal == null) {
            return null;
        }
        return userRepository.findByEmail(principal.getName()).orElse(null);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public List<UserSummaryDto> getAllUserSummaries() {
        return userRepository.findAll().stream()
                .map(this::toSummaryDto)
                .collect(Collectors.toList());
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public void updatePassword(User user, String rawPassword, PasswordEncoder encoder) {
        user.setPassword(encoder.encode(rawPassword));
        userRepository.save(user);
    }

    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    private UserSummaryDto toSummaryDto(User user) {
        String role = user.getRoles().stream()
                .map(Role::getName)
                .findFirst()
                .map(Enum::name)
                .orElse("");
        return new UserSummaryDto(user.getId(), user.getUsername(), role);
    }
}
