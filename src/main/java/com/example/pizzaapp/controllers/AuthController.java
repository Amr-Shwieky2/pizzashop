package com.example.pizzaapp.controllers;

import com.example.pizzaapp.dtos.RegistrationDto;
import com.example.pizzaapp.models.Role;
import com.example.pizzaapp.models.User;
import com.example.pizzaapp.repositories.RoleRepository;
import com.example.pizzaapp.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) Boolean register, Model model,
            Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/";
        }
        model.addAttribute("showRegister", register != null && register);
        model.addAttribute("registrationDto", new RegistrationDto());
        return "pages/login/index";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegistrationDto dto, RedirectAttributes redirectAttrs) {
        if (dto == null || dto.getPassword() == null || dto.getConfirmPassword() == null ||
                dto.getUsername() == null || dto.getEmail() == null) {
            redirectAttrs.addFlashAttribute("error", "All fields are required.");
            return "redirect:/login?register=true";
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            redirectAttrs.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/login?register=true";
        }

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            redirectAttrs.addFlashAttribute("error", "Username already exists.");
            return "redirect:/login?register=true";
        }

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            redirectAttrs.addFlashAttribute("error", "Email already in use.");
            return "redirect:/login?register=true";
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("Default role USER not found"));
        user.getRoles().add(userRole);

        userRepository.save(user);

        redirectAttrs.addFlashAttribute("success", "Registration successful! You can now log in.");
        return "redirect:/login";
    }
}