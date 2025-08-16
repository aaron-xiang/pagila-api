package com.example.pagila_api;

import com.example.pagila_api.config.TestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base test class for H2 database testing.
 * 
 * This class provides:
 * - H2 in-memory database configuration
 * - Test profile activation
 * - Disabled security for easier testing
 * - Transactional rollback for test isolation
 * 
 * Usage:
 * Extend this class instead of using @SpringBootTest directly to automatically
 * get H2 database configuration and avoid Testcontainers.
 * 
 * Example:
 * <pre>
 * class MyServiceTest extends BaseH2Test {
 *     // Your tests here will use H2 database
 * }
 * </pre>
 */
@SpringBootTest(
    classes = {PagilaApiApplication.class, TestConfig.class},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@Transactional
public abstract class BaseH2Test {
    
    // This class serves as a base for other test classes
    // No additional implementation needed - just extend this class
    // in your test classes to get H2 configuration automatically
}