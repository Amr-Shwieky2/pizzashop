package com.example.pizzaapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class OrderItemToppingId implements Serializable {

    @Column(name = "order_item_id", nullable = false)
    private UUID orderItemId;

    @Column(name = "topping_id", nullable = false)
    private UUID toppingId;

    public OrderItemToppingId() {
    }

    public OrderItemToppingId(UUID orderItemId, UUID toppingId) {
        this.orderItemId = orderItemId;
        this.toppingId = toppingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderItemToppingId that = (OrderItemToppingId) o;
        return Objects.equals(orderItemId, that.orderItemId) &&
                Objects.equals(toppingId, that.toppingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItemId, toppingId);
    }
}