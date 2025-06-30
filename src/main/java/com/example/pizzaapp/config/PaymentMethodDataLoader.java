package com.example.pizzaapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.pizzaapp.enums.PaymentMethodEnum;
import com.example.pizzaapp.models.PaymentMethod;
import com.example.pizzaapp.repositories.PaymentMethodRepository;

@Component
@Order(1) // Ensure this runs before other data loaders
public class PaymentMethodDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(PaymentMethodDataLoader.class);

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public void run(String... args) throws Exception {
        loadPaymentMethods();
    }

    private void loadPaymentMethods() {
        logger.info("Loading payment methods...");

        // Check if payment methods already exist
        if (paymentMethodRepository.count() > 0) {
            logger.info("Payment methods already exist in database, skipping initialization");
            return;
        }

        // Create and save all payment method enum values
        for (PaymentMethodEnum paymentMethodEnum : PaymentMethodEnum.values()) {
            if (!paymentMethodRepository.existsByCode(paymentMethodEnum)) {
                PaymentMethod paymentMethod = new PaymentMethod(paymentMethodEnum);
                paymentMethodRepository.save(paymentMethod);
                logger.info("Created payment method: {}", paymentMethodEnum);
            }
        }

        logger.info("Payment methods loading completed");
    }

}
