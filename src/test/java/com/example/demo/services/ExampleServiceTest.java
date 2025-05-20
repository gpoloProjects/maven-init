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
        dog1 = Dog.builder().id(1L).name("Rex").breed("German Shepherd").build();
        dog2 = Dog.builder().id(2L).name("Buddy").breed("Golden Retriever").build();
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
        assertThat(result).contains(dog1, dog2);
        verify(dogRepository, times(1)).findAll();
    }

    @Test
    void testGetDogById() {
        // Given
        when(dogRepository.findById(1L)).thenReturn(Optional.of(dog1));

        // When
        Optional<Dog> result = exampleService.getDogById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getName()).isEqualTo("Rex");
        verify(dogRepository, times(1)).findById(1L);
    }

    @Test
    void testGetDogByIdNotFound() {
        // Given
        when(dogRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Dog> result = exampleService.getDogById(999L);

        // Then
        assertThat(result).isEmpty();
        verify(dogRepository, times(1)).findById(999L);
    }

    @Test
    void testCreateDog() {
        // Given
        Dog newDog = Dog.builder().name("Max").breed("Bulldog").build();
        Dog savedDog = Dog.builder().id(3L).name("Max").breed("Bulldog").build();
        when(dogRepository.save(any(Dog.class))).thenReturn(savedDog);

        // When
        Dog result = exampleService.createDog(newDog);

        // Then
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("Max");
        assertThat(result.getBreed()).isEqualTo("Bulldog");
        verify(dogRepository, times(1)).save(any(Dog.class));
    }

    @Test
    void testDeleteDog() {
        // Given
        doNothing().when(dogRepository).deleteById(anyLong());

        // When
        exampleService.deleteDog(1L);

        // Then
        verify(dogRepository, times(1)).deleteById(1L);
    }
}