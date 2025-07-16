package com.example.pagila_api.integration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class BaseIntegrationTest {

//    @Container
//    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
//            .withDatabaseName("pagila_test")
//            .withUsername("test")
//            .withPassword("test")
//            .withInitScript("test-data.sql"); // Optional: load test data
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgres::getJdbcUrl);
//        registry.add("spring.datasource.username", postgres::getUsername);
//        registry.add("spring.datasource.password", postgres::getPassword);
//        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
//
//        // Disable security for tests (or configure test security)
//        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> "");
//        registry.add("jwt.secret", () -> "test-secret-key-for-testing-purposes-only");
//    }
//
//    @BeforeAll
//    static void beforeAll() {
//        postgres.start();
//    }
}