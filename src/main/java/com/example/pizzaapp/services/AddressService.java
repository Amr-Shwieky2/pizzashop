package com.example.pizzaapp.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pizzaapp.models.DeliveryAddress;
import com.example.pizzaapp.models.User;
import com.example.pizzaapp.repositories.UserRepository;

@Service
public class AddressService {

    @Autowired
    private UserRepository userRepository;

    public void saveDeliveryAddress(DeliveryAddress deliveryAddress, User user) {
        deliveryAddress.setUser(user);
        user.getDeliveryAddresses().add(deliveryAddress);
        userRepository.save(user);
    }

    public void deleteAddress(UUID addressId, User user) {
        user.getDeliveryAddresses().removeIf(addr -> addr.getId().equals(addressId));
        userRepository.save(user);
    }

    public DeliveryAddress findAddressById(UUID addressId, User user) {
        return user.getDeliveryAddresses().stream()
                .filter(addr -> addr.getId().equals(addressId))
                .findFirst()
                .orElse(null);
    }
}
