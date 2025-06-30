package com.example.pizzaapp.models;

import jakarta.persistence.*;
import lombok.*;

import com.example.pizzaapp.enums.ProductSize;
import com.example.pizzaapp.utils.UUIDv7Generator;

import java.time.LocalDateTime;
import java.util.*;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    private String name;
    private String description;
    private String imageUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOption> options = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "product_allowed_toppings", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "topping_id"))
    private Set<Topping> allowedToppings = new HashSet<>();

    public Product(String name, String description, ProductCategory category, String imageUrl,
            Set<Topping> allowedToppings) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
        this.allowedToppings = allowedToppings;
    }

    @PrePersist
    public void ensureId() {
        if (this.id == null) {
            this.id = UUIDv7Generator.generate();
        }
    }

    public ProductSize getDefaultSize() {
        return ProductSize.SMALL;
    }

    public Double getDefaultPrice() {
        if (options != null && !options.isEmpty()) {
            for (ProductOption option : options) {
                if (option.getSize() == getDefaultSize()) {
                    return option.getPrice();
                }
            }
        }
        return null;
    }

}
