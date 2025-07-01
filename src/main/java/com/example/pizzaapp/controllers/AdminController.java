package com.example.pizzaapp.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.pizzaapp.dtos.OrderSummaryDto;
import com.example.pizzaapp.dtos.UserSummaryDto;
import com.example.pizzaapp.models.OrderStatus.OrderStatusEnum;
import com.example.pizzaapp.models.User;
import com.example.pizzaapp.services.OrderService;
import com.example.pizzaapp.services.UserService;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final OrderService orderService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService userService, OrderService orderService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.orderService = orderService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/adminDashboard";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<UserSummaryDto> users = userService.getAllUserSummaries();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping("/users/{id}/orders")
    public String userOrders(@PathVariable UUID id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            return "pages/error/404";
        }
        List<OrderSummaryDto> orders = orderService.getUserOrderSummaries(user);
        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        return "admin/userOrders";
    }

    @GetMapping("/users/{id}/changePassword")
    public String changePasswordForm(@PathVariable UUID id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            return "pages/error/404";
        }
        model.addAttribute("userId", user.getId());
        return "admin/changeUserPassword";
    }

    @PostMapping("/users/changePassword")
    public String changePassword(@RequestParam UUID userId,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/admin/users/" + userId + "/changePassword";
        }
        User user = userService.findById(userId);
        if (user == null) {
            return "pages/error/404";
        }
        userService.updatePassword(user, newPassword, passwordEncoder);
        redirectAttributes.addFlashAttribute("success", "Password changed successfully");
        return "redirect:/admin/users";
    }

    @PostMapping("/updateOrderStatus")
    public String updateOrderStatus(@RequestParam UUID orderId,
                                    @RequestParam("status") OrderStatusEnum status) {
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/admin/orders";
    }

    @GetMapping("/orders")
    public String allOrders(Model model) {
        List<OrderSummaryDto> orders = orderService.getAllOrderSummaries();
        model.addAttribute("orders", orders);
        return "admin/orders";
    }
}
