package com.example.pizzaapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.ModelMapper;

/**
 * Configuration class for application-wide beans.
 */
@Configuration
public class AppConfig {

    /**
     * Creates and configures a ModelMapper bean for object mapping.
     * 
     * @return a new instance of ModelMapper
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}