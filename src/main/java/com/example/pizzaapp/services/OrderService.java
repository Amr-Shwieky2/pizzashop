package com.example.pizzaapp.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pizzaapp.dtos.CartItemDto;
import com.example.pizzaapp.dtos.OrderDetailDto;
import com.example.pizzaapp.dtos.OrderItemDetailDto;
import com.example.pizzaapp.dtos.OrderSummaryDto;
import com.example.pizzaapp.dtos.OrderToppingDto;
import com.example.pizzaapp.models.Order;
import com.example.pizzaapp.models.OrderItem;
import com.example.pizzaapp.models.OrderItemTopping;
import com.example.pizzaapp.models.OrderStatus;
import com.example.pizzaapp.models.PaymentMethod;
import com.example.pizzaapp.models.Product;
import com.example.pizzaapp.models.ProductOption;
import com.example.pizzaapp.models.Topping;
import com.example.pizzaapp.models.User;
import com.example.pizzaapp.repositories.OrderRepository;
import com.example.pizzaapp.repositories.OrderStatusRepository;
import com.example.pizzaapp.repositories.ProductOptionRepository;
import com.example.pizzaapp.repositories.ProductRepository;
import com.example.pizzaapp.repositories.ToppingRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private ToppingRepository toppingRepository;

    @Autowired
    private PricingService pricingService;

    public Order createAndSaveOrder(User user, List<CartItemDto> cartItems, String deliveryAddress,
            PaymentMethod paymentMethod) {
        // Build OrderItem objects
        List<OrderItem> orderItems = buildOrderItems(cartItems);

        // Calculate total price including delivery and tax
        double totalPrice = pricingService.calculateTotalOrderPrice(orderItems);

        // Get the default order status (PENDING)
        OrderStatus pendingStatus = orderStatusRepository
                .findByCode(OrderStatus.OrderStatusEnum.PENDING)
                .orElseThrow(() -> new IllegalStateException("PENDING order status not found in database"));

        // Create and save the order
        Order order = new Order(user, totalPrice, orderItems, deliveryAddress, paymentMethod);
        order.setStatus(pendingStatus);

        // Set the order reference for each OrderItem
        orderItems.forEach(item -> item.setOrder(order));

        return orderRepository.save(order);
    }

    public List<OrderItem> buildOrderItems(List<CartItemDto> cartItems) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItemDto cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + cartItem.getProductId()));

            ProductOption productOption = productOptionRepository
                    .findByProductIdAndSize(product.getId(), cartItem.getSize())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid size for product: " + cartItem.getSize()));

            List<Topping> toppings = toppingRepository.findAllById(cartItem.getToppingIds());

            double basePrice = productOption.getPrice();
            double toppingsPrice = toppings.stream().mapToDouble(Topping::getPrice).sum();
            double subtotal = basePrice + toppingsPrice;

            // Create OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setSize(cartItem.getSize());
            orderItem.setBasePrice(basePrice);
            orderItem.setSubtotal(subtotal);

            // Create OrderItemTopping objects
            Set<OrderItemTopping> orderItemToppings = new HashSet<>();
            for (Topping topping : toppings) {
                OrderItemTopping orderItemTopping = new OrderItemTopping(orderItem, topping, topping.getPrice());
                orderItemToppings.add(orderItemTopping);
            }
            orderItem.setOrderItemToppings(orderItemToppings);

            orderItems.add(orderItem);
        }

        return orderItems;
    }

    /**
     * Get all orders for a specific user as summary DTOs
     */
    public List<OrderSummaryDto> getUserOrderSummaries(User user) {
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream()
                .map(this::mapToOrderSummaryDto)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific order by ID if it belongs to the user
     */
    public Optional<OrderDetailDto> getUserOrderDetail(UUID orderId, User user) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);

        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            // Check if the order belongs to the user
            if (order.getUser().getId().equals(user.getId())) {
                return Optional.of(mapToOrderDetailDto(order));
            }
        }

        return Optional.empty();
    }

    /**
     * Get a specific order by ID (for admin use)
     */
    public Optional<OrderDetailDto> getOrderDetail(UUID orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        return orderOpt.map(this::mapToOrderDetailDto);
    }

    /**
     * Get all orders (for admin use)
     */
    public List<OrderSummaryDto> getAllOrderSummaries() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::mapToOrderSummaryDto)
                .collect(Collectors.toList());
    }

    private OrderSummaryDto mapToOrderSummaryDto(Order order) {
        return new OrderSummaryDto(
                order.getId(),
                order.getCreatedAt(),
                order.getStatus().getCode().name(),
                order.getPaymentMethod().getCode().name(),
                order.getTotalPrice(),
                order.getItems().size(),
                order.getDeliveryAddress());
    }

    private OrderDetailDto mapToOrderDetailDto(Order order) {
        List<OrderItemDetailDto> itemDtos = order.getItems().stream()
                .map(this::mapToOrderItemDetailDto)
                .collect(Collectors.toList());

        return new OrderDetailDto(
                order.getId(),
                order.getCreatedAt(),
                order.getStatus().getCode().name(),
                order.getPaymentMethod().getCode().name(),
                order.getTotalPrice(),
                order.getDeliveryAddress(),
                order.getUser().getUsername(),
                order.getUser().getEmail(),
                itemDtos);
    }

    private OrderItemDetailDto mapToOrderItemDetailDto(OrderItem item) {
        List<OrderToppingDto> toppingDtos = item.getOrderItemToppings().stream()
                .map(oit -> new OrderToppingDto(oit.getTopping().getName(), oit.getPrice()))
                .collect(Collectors.toList());

        return new OrderItemDetailDto(
                item.getProduct().getName(),
                item.getProduct().getCategory().getName(),
                item.getSize().name(),
                item.getBasePrice(),
                item.getSubtotal(),
                toppingDtos);
    }
}
