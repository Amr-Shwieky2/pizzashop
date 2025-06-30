package com.example.pizzaapp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pizzaapp.models.OrderItem;

@Service
public class PricingService {

    private static final double DELIVERY_CHARGE = 10.0;
    private static final double TAX_RATE = 0.17;

    public double calculateTotalOrderPrice(List<OrderItem> orderItems) {
        double subtotal = orderItems.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();

        double tax = subtotal * TAX_RATE;

        return subtotal + DELIVERY_CHARGE + tax;
    }

    public double calculateSubtotal(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
    }

    public double getDeliveryCharge() {
        return DELIVERY_CHARGE;
    }

    public double calculateTax(double subtotal) {
        return subtotal * TAX_RATE;
    }

    public double getTaxRate() {
        return TAX_RATE;
    }
}
