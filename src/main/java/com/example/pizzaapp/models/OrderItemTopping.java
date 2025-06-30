package com.example.pizzaapp.models;

import jakarta.persistence.*;

@Entity
@Table(name = "order_item_topping")
public class OrderItemTopping {

    @EmbeddedId
    private OrderItemToppingId id = new OrderItemToppingId();

    @ManyToOne
    @MapsId("orderItemId")
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @ManyToOne
    @MapsId("toppingId")
    @JoinColumn(name = "topping_id")
    private Topping topping;

    @Column(nullable = false)
    private Double price;

    public OrderItemTopping(OrderItem orderItem, Topping topping, Double price) {
        this.orderItem = orderItem;
        this.topping = topping;
        this.price = price;
        this.id = new OrderItemToppingId(orderItem.getId(), topping.getId());
    }

    public OrderItemTopping() {
    }

    // Getters and setters
    public OrderItemToppingId getId() {
        return id;
    }

    public void setId(OrderItemToppingId id) {
        this.id = id;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public Topping getTopping() {
        return topping;
    }

    public void setTopping(Topping topping) {
        this.topping = topping;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
