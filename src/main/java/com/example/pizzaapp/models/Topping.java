package com.example.pizzaapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.example.pizzaapp.utils.UUIDv7Generator;

@Entity
@Table(name = "toppings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Topping {
    @Id
    private UUID id;

    private String name;

    private Double price;

    private String imageUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Topping(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Topping(String name, Double price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    @PrePersist
    public void ensureId() {
        if (this.id == null) {
            this.id = UUIDv7Generator.generate();
        }
    }
}
