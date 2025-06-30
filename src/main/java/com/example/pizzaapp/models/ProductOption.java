package com.example.pizzaapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.example.pizzaapp.enums.ProductSize;
import com.example.pizzaapp.utils.UUIDv7Generator;

@Entity
@Table(name = "product_options")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOption {
    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ProductSize size;

    private Double price;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductOption(Product product, ProductSize size, Double price) {
        this.product = product;
        this.size = size;
        this.price = price;
    }

    @PrePersist
    public void ensureId() {
        if (this.id == null) {
            this.id = UUIDv7Generator.generate();
        }
    }

    public Double getDefaultPrice() {
        return price != null ? price : 0.0;
    }
}