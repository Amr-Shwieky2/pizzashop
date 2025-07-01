package com.example.pizzaapp.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/error", "/cart", "/menu", "/css/**", "/js/**",
                                                                "/assets/**", "/images/**", "/login", "/register")
                                                .permitAll()
                                                .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .usernameParameter("email")
                                                .passwordParameter("password")
                                                .successHandler((request, response, authentication) -> {
                                                        RequestCache requestCache = new HttpSessionRequestCache();
                                                        var savedRequest = requestCache.getRequest(request, response);

                                                        try {
                                                                if (savedRequest != null) {
                                                                        URI uri = new URI(
                                                                                        savedRequest.getRedirectUrl());
                                                                        String path = uri.getPath();

                                                                        if (path.equals("/cart")
                                                                                        || path.equals("/checkout")) {
                                                                                response.sendRedirect(savedRequest
                                                                                                .getRedirectUrl());
                                                                                return;
                                                                        }
                                                                }

                                                                response.sendRedirect("/");

                                                        } catch (URISyntaxException e) {
                                                                response.sendRedirect("/");
                                                        }
                                                })
                                                .failureUrl("/login?error")
                                                .permitAll())
                                .logout(logout -> logout.permitAll());

                return http.build();
        }
}
