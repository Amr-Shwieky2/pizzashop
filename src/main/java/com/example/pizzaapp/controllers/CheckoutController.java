package com.example.pizzaapp.controllers;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.pizzaapp.dtos.CartItemDto;
import com.example.pizzaapp.models.DeliveryAddress;
import com.example.pizzaapp.models.PaymentMethod;
import com.example.pizzaapp.models.User;
import com.example.pizzaapp.services.AddressService;
import com.example.pizzaapp.services.CartService;
import com.example.pizzaapp.services.OrderService;
import com.example.pizzaapp.services.PaymentService;
import com.example.pizzaapp.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class CheckoutController {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/checkout")
    public String checkout(Model model, Principal principal) {
        User user = userService.findUserByPrincipal(principal);
        if (user != null) {
            List<DeliveryAddress> addresses = user.getDeliveryAddresses();
            model.addAttribute("addresses", addresses);
        }

        model.addAttribute("newAddress", new DeliveryAddress());
        return "pages/checkout/index";
    }

    @PostMapping("/checkout/address")
    public String saveDeliveryAddress(@ModelAttribute DeliveryAddress deliveryAddress,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        User user = userService.findUserByPrincipal(principal);
        if (user != null) {
            addressService.saveDeliveryAddress(deliveryAddress, user);
            redirectAttributes.addFlashAttribute("success", "Delivery address saved successfully!");
        }
        return "redirect:/checkout";
    }

    @PostMapping("/checkout/address/{id}/delete")
    public String deleteAddress(@PathVariable UUID id, Principal principal, RedirectAttributes redirectAttributes) {
        User user = userService.findUserByPrincipal(principal);
        if (user != null) {
            addressService.deleteAddress(id, user);
            redirectAttributes.addFlashAttribute("success", "Address deleted.");
        }
        return "redirect:/checkout";
    }

    @PostMapping("/checkout/submit")
    public String submitOrder(
            @RequestParam("selectedAddress") UUID addressId,
            @RequestParam("pay-method") String paymentMethodCode,
            @RequestParam("billing-name") String billingName,
            @RequestParam("billing-phone") String billingPhone,
            @RequestParam("cartJson") String cartJson,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        try {
            // Validate user authentication
            User user = userService.findUserByPrincipal(principal);
            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "You must be logged in to place an order.");
                return "redirect:/login";
            }

            // Find selected delivery address
            DeliveryAddress selectedAddress = addressService.findAddressById(addressId, user);
            if (selectedAddress == null) {
                redirectAttributes.addFlashAttribute("error", "Invalid delivery address selected.");
                return "redirect:/checkout";
            }

            // Parse and validate cart items
            List<CartItemDto> cartItems = cartService.parseCartItems(cartJson);
            if (cartService.isCartEmpty(cartItems)) {
                redirectAttributes.addFlashAttribute("error", "Your cart is empty.");
                return "redirect:/checkout";
            }

            // Validate payment method
            PaymentMethod paymentMethod = paymentService.findPaymentMethodByCode(paymentMethodCode);
            if (paymentMethod == null) {
                redirectAttributes.addFlashAttribute("error", "Invalid payment method selected.");
                return "redirect:/checkout";
            }

            // Create and save the order
            orderService.createAndSaveOrder(user, cartItems, selectedAddress.getFullAddress(), paymentMethod);

            redirectAttributes.addFlashAttribute("success", "Order placed successfully!");
            return "redirect:/order/confirmation";

        } catch (Exception e) {
            logger.error("Error processing order submission", e);
            redirectAttributes.addFlashAttribute("error", "Failed to process cart items.");
            return "redirect:/checkout";
        }
    }

    /**
     * Order confirmation page
     */
    @GetMapping("/order/confirmation")
    public String orderConfirmation() {
        return "pages/order/confirmation";
    }
}
