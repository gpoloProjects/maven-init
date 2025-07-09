package com.example.demo.services;

import com.example.demo.models.Dog;
import com.example.demo.models.DogMapper;
import com.example.demo.models.DogRequestDto;
import com.example.demo.models.DogResponseDto;
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

    @Mock
    private DogMapper dogMapper;

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

    @Test
    void testDeleteDog() {
        // Given
        String dogId = "1";

        // When
        exampleService.deleteDog(dogId);

        // Then
        verify(dogRepository, times(1)).deleteById(dogId);
    }

    @Test
    void testDeleteDogNotFound() {
        // Given
        String nonExistentId = "999";

        // When
        exampleService.deleteDog(nonExistentId);

        // Then
        verify(dogRepository, times(1)).deleteById(nonExistentId);
    }

    // New tests for DTO-based service methods
    @Test
    void testGetAllDogsAsDto() {
        // Given
        List<Dog> dogs = Arrays.asList(dog1, dog2);
        when(dogRepository.findAll()).thenReturn(dogs);
        when(dogMapper.toDogResponseDtoList(dogs)).thenReturn(Arrays.asList(
                DogResponseDto.builder().id("1").name("Rex").breed("German Shepherd").build(),
                DogResponseDto.builder().id("2").name("Buddy").breed("Golden Retriever").build()
        ));

        // When
        List<DogResponseDto> result = exampleService.getAllDogsAsDto();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Rex");
        assertThat(result.get(1).getName()).isEqualTo("Buddy");
        verify(dogRepository, times(1)).findAll();
        verify(dogMapper, times(1)).toDogResponseDtoList(dogs);
    }

    @Test
    void testGetDogByIdAsDto() {
        // Given
        when(dogRepository.findById("1")).thenReturn(Optional.of(dog1));
        DogResponseDto expectedDto = DogResponseDto.builder()
                .id("1").name("Rex").breed("German Shepherd").build();
        when(dogMapper.toDogResponseDto(dog1)).thenReturn(expectedDto);

        // When
        DogResponseDto result = exampleService.getDogByIdAsDto("1");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getName()).isEqualTo("Rex");
        verify(dogRepository, times(1)).findById("1");
        verify(dogMapper, times(1)).toDogResponseDto(dog1);
    }

    @Test
    void testCreateDogFromDto() {
        // Given
        DogRequestDto requestDto = DogRequestDto.builder()
                .name("Max").breed("Bulldog").build();
        Dog newDog = Dog.builder().name("Max").breed("Bulldog").build();
        Dog savedDog = Dog.builder().id("3").name("Max").breed("Bulldog").build();
        DogResponseDto expectedDto = DogResponseDto.builder()
                .id("3").name("Max").breed("Bulldog").build();

        when(dogMapper.toDogEntity(requestDto)).thenReturn(newDog);
        when(dogRepository.save(newDog)).thenReturn(savedDog);
        when(dogMapper.toDogResponseDto(savedDog)).thenReturn(expectedDto);

        // When
        DogResponseDto result = exampleService.createDogFromDto(requestDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("3");
        assertThat(result.getName()).isEqualTo("Max");
        verify(dogMapper, times(1)).toDogEntity(requestDto);
        verify(dogRepository, times(1)).save(newDog);
        verify(dogMapper, times(1)).toDogResponseDto(savedDog);
    }
}