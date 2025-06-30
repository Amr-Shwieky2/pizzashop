package com.example.pizzaapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling home-related requests.
 */
@Controller
public class IndexController {

    /**
     * Displays the home page.
     * 
     * @return the home view
     */
    @GetMapping("/")
    public String viewHome() {
        return "index";
    }

}
