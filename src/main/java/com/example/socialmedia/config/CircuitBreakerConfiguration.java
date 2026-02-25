package com.example.socialmedia.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Circuit Breaker Configuration using Resilience4j.
 *
 * Provides a shared registry with named circuit breakers for each service.
 * When a downstream service fails repeatedly, the circuit opens and
 * requests fail fast instead of hanging — this is the "when a website
 * breaks, then what?" answer.
 *
 * Services can inject CircuitBreakerRegistry and use:
 * CircuitBreaker cb = registry.circuitBreaker("chat-service");
 * cb.executeSupplier(() -> someCall());
 */
@Configuration
public class CircuitBreakerConfiguration {

    private static final Logger log = LoggerFactory.getLogger(CircuitBreakerConfiguration.class);

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        // Default config shared by all circuit breakers
        CircuitBreakerConfig defaultConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50) // open after 50% failures
                .waitDurationInOpenState(Duration.ofSeconds(30)) // wait 30s before half-open
                .slidingWindowSize(10) // evaluate last 10 calls
                .minimumNumberOfCalls(5) // need at least 5 calls
                .automaticTransitionFromOpenToHalfOpenEnabled(true) // auto-retry after wait
                .build();

        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(defaultConfig);

        // Pre-register named circuit breakers for each service module
        registry.circuitBreaker("chat-service");
        registry.circuitBreaker("notification-service");
        registry.circuitBreaker("media-upload-service");
        registry.circuitBreaker("email-service");

        log.info("CircuitBreakerRegistry initialized with services: {}",
                registry.getAllCircuitBreakers().stream()
                        .map(cb -> cb.getName())
                        .toList());

        return registry;
    }
}
