package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

class DemoApplicationMainTest {

    @Test
    void testMain() {
        // Use mockStatic to prevent actual Spring context loading
        try (var mockedSpringApplication = mockStatic(SpringApplication.class)) {
            // Call the main method
            DemoApplication.main(new String[]{});

            // Verify that SpringApplication.run was called with correct parameters
            mockedSpringApplication.verify(() ->
                    SpringApplication.run(eq(DemoApplication.class), any(String[].class)));
        }
    }
}