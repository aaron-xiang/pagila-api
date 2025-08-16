package com.example.pagila_api.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test configuration that disables security and configures H2 database for testing.
 * This configuration is automatically applied when the 'test' profile is active.
 */
@TestConfiguration
@EnableWebSecurity
@Profile("test")
public class TestConfig {

    /**
     * Disable security for tests to simplify testing without authentication.
     * This bean will override the main security configuration when testing.
     */
    @Bean
    @Primary
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().permitAll()
            )
            .headers(headers -> headers
                .frameOptions().sameOrigin() // Allow H2 console frames
            );
        
        return http.build();
    }
}