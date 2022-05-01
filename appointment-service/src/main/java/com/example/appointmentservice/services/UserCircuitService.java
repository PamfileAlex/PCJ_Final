package com.example.appointmentservice.services;

import com.example.appointmentservice.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Service
public class UserCircuitService {
    private final WebClient webClient;
    private final ReactiveCircuitBreaker reactiveCircuitBreaker;

    @Autowired
    public UserCircuitService(ReactiveCircuitBreakerFactory circuitBreakerFactory) {
        this.webClient = WebClient.builder().baseUrl("http://localhost:8081").build();
        this.reactiveCircuitBreaker = circuitBreakerFactory.create("users");
    }

    public User getById(String id) {

        Flux<User> users = reactiveCircuitBreaker.run(webClient.get().uri("/users").retrieve().bodyToFlux(User.class),
                throwable ->
                {
                    throw new RuntimeException("connection refused");
                });

        Optional<User> user = users.toStream().filter(u -> u.getId().equals(id)).findFirst();
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return user.get();
    }
}
