package com.example.pizzaapp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.example.pizzaapp.utils.UUIDv7Generator;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "status_code")
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "payment_method_code", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    private Double totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    public Order(User user, Double totalPrice, List<OrderItem> items, String deliveryAddress,
            PaymentMethod paymentMethod) {
        this.user = user;
        this.totalPrice = totalPrice;
        this.items = items;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
    }

    @PrePersist
    public void ensureId() {
        if (this.id == null) {
            this.id = UUIDv7Generator.generate();
        }
    }
}
