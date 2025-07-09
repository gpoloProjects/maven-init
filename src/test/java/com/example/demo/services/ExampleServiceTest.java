package com.example.demo.services;

import com.example.demo.models.Dog;
import com.example.demo.repositories.DogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExampleServiceTest {

    @Mock
    private DogRepository dogRepository;

    @InjectMocks
    private ExampleService exampleService;

    private Dog dog1;
    private Dog dog2;

    @BeforeEach
    void setUp() {
        // Create test objects in memory (no database required)
        dog1 = Dog.builder().id("1").name("Rex").breed("German Shepherd").build();
        dog2 = Dog.builder().id("2").name("Buddy").breed("Golden Retriever").build();
    }

    @Test
    void testGetAllDogs() {
        // Given
        List<Dog> dogs = Arrays.asList(dog1, dog2);
        when(dogRepository.findAll()).thenReturn(dogs);

        // When
        List<Dog> result = exampleService.getAllDogs();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(dog1, dog2);
        verify(dogRepository, times(1)).findAll();
    }

    @Test
    void testGetDogById() {
        // Given
        when(dogRepository.findById("1")).thenReturn(Optional.of(dog1));

        // When
        Optional<Dog> result = exampleService.getDogById("1");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(dog1);
        verify(dogRepository, times(1)).findById("1");
    }

    @Test
    void testGetDogByIdNotFound() {
        // Given
        when(dogRepository.findById("999")).thenReturn(Optional.empty());

        // When
        Optional<Dog> result = exampleService.getDogById("999");

        // Then
        assertThat(result).isEmpty();
        verify(dogRepository, times(1)).findById("999");
    }

    @Test
    void testCreateDog() {
        // Given
        Dog newDog = Dog.builder().name("Max").breed("Bulldog").build();
        Dog savedDog = Dog.builder().id("3").name("Max").breed("Bulldog").build();
        when(dogRepository.save(any(Dog.class))).thenReturn(savedDog);

        // When
        Dog result = exampleService.createDog(newDog);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("3");
        assertThat(result.getName()).isEqualTo("Max");
        assertThat(result.getBreed()).isEqualTo("Bulldog");
        verify(dogRepository, times(1)).save(any(Dog.class));
    }
}