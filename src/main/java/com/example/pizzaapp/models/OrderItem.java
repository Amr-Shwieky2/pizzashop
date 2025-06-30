package com.example.pizzaapp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.example.pizzaapp.enums.ProductSize;
import com.example.pizzaapp.utils.UUIDv7Generator;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    private ProductSize size;

    @Column(nullable = false)
    private Double basePrice;

    @Column(nullable = false)
    private Double subtotal;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItemTopping> orderItemToppings = new HashSet<>();

    public OrderItem(Order order, Product product, ProductSize size, Double subtotal, Double basePrice,
            Set<OrderItemTopping> toppings) {
        this.order = order;
        this.product = product;
        this.size = size;
        this.subtotal = subtotal;
        this.basePrice = basePrice;
        this.orderItemToppings = toppings;
    }

    @PrePersist
    public void ensureId() {
        if (this.id == null) {
            this.id = UUIDv7Generator.generate();
        }
    }

    public Double getToppingsTotal() {
        return subtotal - basePrice;
    }
}