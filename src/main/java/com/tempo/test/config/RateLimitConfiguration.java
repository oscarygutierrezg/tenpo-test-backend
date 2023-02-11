package com.tempo.test.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfiguration {


    @Value("${app.ms.rate-limit.capacity}")
    private int capacity;

    @Bean
    Bandwidth bandwidth() {
        return Bandwidth.classic(capacity, Refill.greedy(capacity, Duration.ofMinutes(1)));
    }
}