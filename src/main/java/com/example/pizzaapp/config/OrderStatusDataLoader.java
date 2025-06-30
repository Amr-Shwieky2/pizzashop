package com.example.pizzaapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.pizzaapp.models.OrderStatus;
import com.example.pizzaapp.models.OrderStatus.OrderStatusEnum;
import com.example.pizzaapp.repositories.OrderStatusRepository;

@Component
@Order(1) // Run before other data loaders
public class OrderStatusDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(OrderStatusDataLoader.class);

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Loading order statuses...");

        for (OrderStatusEnum statusEnum : OrderStatusEnum.values()) {
            if (!orderStatusRepository.existsByCode(statusEnum)) {
                OrderStatus orderStatus = new OrderStatus(statusEnum);
                orderStatusRepository.save(orderStatus);
                logger.info("Created order status: {}", statusEnum);
            }
        }

        logger.info("Order status loading completed. Total statuses: {}", orderStatusRepository.count());
    }
}
