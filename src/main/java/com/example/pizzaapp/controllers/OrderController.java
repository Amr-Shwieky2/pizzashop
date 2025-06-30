package com.example.pizzaapp.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.pizzaapp.dtos.OrderDetailDto;
import com.example.pizzaapp.dtos.OrderSummaryDto;
import com.example.pizzaapp.models.User;
import com.example.pizzaapp.services.OrderService;
import com.example.pizzaapp.services.UserService;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    /**
     * Display the user's order history page
     */
    @GetMapping
    public String orderHistory(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.findUserByPrincipal(principal);
        if (user == null) {
            return "redirect:/login";
        }

        List<OrderSummaryDto> orders = orderService.getUserOrderSummaries(user);
        model.addAttribute("orders", orders);
        model.addAttribute("user", user);

        return "pages/orders/index";
    }

    /**
     * Display a specific order's details
     */
    @GetMapping("/{orderId}")
    public String orderDetail(@PathVariable UUID orderId, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.findUserByPrincipal(principal);
        if (user == null) {
            return "redirect:/login";
        }

        Optional<OrderDetailDto> orderOpt = orderService.getUserOrderDetail(orderId, user);
        if (orderOpt.isEmpty()) {
            return "pages/error/404";
        }

        model.addAttribute("order", orderOpt.get());
        return "pages/orders/detail";
    }

    /**
     * API endpoint to get user's orders as JSON
     */
    @GetMapping("/api/my-orders")
    @ResponseBody
    public ResponseEntity<List<OrderSummaryDto>> getMyOrders(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        User user = userService.findUserByPrincipal(principal);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        List<OrderSummaryDto> orders = orderService.getUserOrderSummaries(user);
        return ResponseEntity.ok(orders);
    }

    /**
     * API endpoint to get a specific order as JSON
     */
    @GetMapping("/api/{orderId}")
    @ResponseBody
    public ResponseEntity<OrderDetailDto> getOrderDetail(@PathVariable UUID orderId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        User user = userService.findUserByPrincipal(principal);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        Optional<OrderDetailDto> orderOpt = orderService.getUserOrderDetail(orderId, user);
        if (orderOpt.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(orderOpt.get());
    }

    /**
     * Admin endpoint to view all orders
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminOrderHistory(Model model) {
        List<OrderSummaryDto> orders = orderService.getAllOrderSummaries();
        model.addAttribute("orders", orders);
        return "admin/orders";
    }

    /**
     * Admin endpoint to view a specific order
     */
    @GetMapping("/admin/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminOrderDetail(@PathVariable UUID orderId, Model model) {
        Optional<OrderDetailDto> orderOpt = orderService.getOrderDetail(orderId);
        if (orderOpt.isEmpty()) {
            return "pages/error/404";
        }

        model.addAttribute("order", orderOpt.get());
        return "admin/orderDetail";
    }

    /**
     * Admin API endpoint to get all orders as JSON
     */
    @GetMapping("/api/admin/all-orders")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderSummaryDto>> getAllOrders() {
        List<OrderSummaryDto> orders = orderService.getAllOrderSummaries();
        return ResponseEntity.ok(orders);
    }

    /**
     * Admin API endpoint to get a specific order as JSON
     */
    @GetMapping("/api/admin/{orderId}")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDetailDto> getAdminOrderDetail(@PathVariable UUID orderId) {
        Optional<OrderDetailDto> orderOpt = orderService.getOrderDetail(orderId);
        if (orderOpt.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(orderOpt.get());
    }
}
