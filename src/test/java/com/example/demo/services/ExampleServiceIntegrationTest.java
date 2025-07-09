package com.example.demo.services;

import com.example.demo.models.Dog;
import com.example.demo.repositories.DogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for ExampleService that uses real embedded MongoDB.
 * This test may not work in corporate environments with strict security policies.
 * Use ExampleServiceTest for unit testing in restrictive environments.
 */
@DataMongoTest
@Import(ExampleService.class)
@ActiveProfiles("test")
public class ExampleServiceIntegrationTest {

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private ExampleService exampleService;

    private Dog dog1;
    private Dog dog2;

    @BeforeEach
    void setUp() {
        // Clear existing data
        dogRepository.deleteAll();

        // Create and save dog1 to the database
        Dog newDog1 = Dog.builder().name("Rex").breed("German Shepherd").build();
        dog1 = dogRepository.save(newDog1);

        // Create and save dog2 to the database too
        Dog newDog2 = Dog.builder().name("Buddy").breed("Golden Retriever").build();
        dog2 = dogRepository.save(newDog2);
    }

    @Test
    void testGetAllDogsWithRealDatabase() {
        // When
        List<Dog> result = exampleService.getAllDogs();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrder(dog1, dog2);
    }

    @Test
    void testGetDogByIdWithRealDatabase() {
        // When
        Optional<Dog> result = exampleService.getDogById(dog1.getId());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(dog1);
    }

    @Test
    void testCreateDogWithRealDatabase() {
        // Given
        Dog newDog = Dog.builder().name("Max").breed("Bulldog").build();

        // When
        Dog result = exampleService.createDog(newDog);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Max");
        assertThat(result.getBreed()).isEqualTo("Bulldog");

        // Verify it was actually saved to the database
        Optional<Dog> fromDb = dogRepository.findById(result.getId());
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get().getName()).isEqualTo("Max");
    }
}
