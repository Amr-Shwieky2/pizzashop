package com.example.pizzaapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.example.pizzaapp.utils.UUIDv7Generator;

@Entity
@Table(name = "delivery_addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddress {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String streetAddress;

    private String apartment;
    private String landmark;

    @Column(nullable = false)
    private String city;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    public void ensureId() {
        if (this.id == null) {
            this.id = UUIDv7Generator.generate();
        }
    }

    public String getFullAddress() {
        return streetAddress + (apartment != null ? ", Apt " + apartment : "") + ", " + city
                + (landmark != null ? " (Near " + landmark + ")" : "");
    }
}
