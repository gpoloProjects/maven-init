package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.services.ExampleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExampleController.class)
public class ExampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ExampleService exampleService;

    @MockitoBean
    private DogMapper dogMapper;

    @Test
    public void getAllDogs_ShouldReturnDogs() throws Exception {
        // Arrange
        Dog dog1 = new Dog(1L, "Labrador", "Max");
        Dog dog2 = new Dog(2L, "Beagle", "Charlie");
        List<Dog> dogs = Arrays.asList(dog1, dog2);

        DogResponseDto dto1 = new DogResponseDto();
        dto1.setId(1L);
        dto1.setBreed("Labrador");
        dto1.setName("Max");

        DogResponseDto dto2 = new DogResponseDto();
        dto2.setId(2L);
        dto2.setBreed("Beagle");
        dto2.setName("Charlie");
        List<DogResponseDto> dtos = Arrays.asList(dto1, dto2);

        when(exampleService.getAllDogs()).thenReturn(dogs);
        when(dogMapper.toDogResponseDtoList(dogs)).thenReturn(dtos);

        // Act & Assert
        mockMvc.perform(get("/v1/examples/dogs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].breed").value("Labrador"))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    public void getDogById_WhenDogExists_ShouldReturnDog() throws Exception {
        // Arrange
        Long id = 1L;
        Dog dog = new Dog(id, "Labrador", "Max");
        DogResponseDto dto = new DogResponseDto();
        dto.setId(id);
        dto.setBreed("Labrador");
        dto.setName("Max");

        when(exampleService.getDogById(id)).thenReturn(Optional.of(dog));
        when(dogMapper.toDogResponseDto(dog)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/v1/examples/dogs/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.breed").value("Labrador"))
                .andExpect(jsonPath("$.name").value("Max"));
    }

    @Test
    public void getDogById_WhenDogNotExists_ShouldReturn404() throws Exception {
        // Arrange
        Long id = 1L;
        when(exampleService.getDogById(id)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/v1/examples/dogs/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createDog_WithValidData_ShouldReturnNewDog() throws Exception {
        // Arrange
        DogRequestDto requestDto = new DogRequestDto();
        requestDto.setBreed("Labrador");
        requestDto.setName("Max");

        Dog dog = new Dog();
        dog.setBreed("Labrador");
        dog.setName("Max");

        Dog savedDog = new Dog();
        savedDog.setId(1L);
        savedDog.setBreed("Labrador");
        savedDog.setName("Max");

        DogResponseDto responseDto = new DogResponseDto();
        responseDto.setId(1L);
        responseDto.setBreed("Labrador");
        responseDto.setName("Max");

        when(dogMapper.toDogEntity(any(DogRequestDto.class))).thenReturn(dog);
        when(exampleService.createDog(any(Dog.class))).thenReturn(savedDog);
        when(dogMapper.toDogResponseDto(any(Dog.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/v1/examples/dogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.breed").value("Labrador"))
                .andExpect(jsonPath("$.name").value("Max"));
    }

    @Test
    public void updateDog_WithValidData_ShouldReturnUpdatedDog() throws Exception {
        // Arrange
        Long id = 1L;
        DogRequestDto requestDto = new DogRequestDto();
        requestDto.setBreed("Labrador");
        requestDto.setName("Updated Max");

        Dog existingDog = new Dog();
        existingDog.setId(id);
        existingDog.setBreed("Labrador");
        existingDog.setName("Max");

        Dog updatedDog = new Dog();
        updatedDog.setId(id);
        updatedDog.setBreed("Labrador");
        updatedDog.setName("Updated Max");

        DogResponseDto responseDto = new DogResponseDto();
        responseDto.setId(id);
        responseDto.setBreed("Labrador");
        responseDto.setName("Updated Max");

        when(exampleService.getDogById(id)).thenReturn(Optional.of(existingDog));
        when(exampleService.createDog(any(Dog.class))).thenReturn(updatedDog);
        when(dogMapper.toDogResponseDto(updatedDog)).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(put("/v1/examples/dogs/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Updated Max"));

        verify(dogMapper).updateDogEntity(any(Dog.class), any(DogRequestDto.class));
    }

    @Test
    public void deleteDog_WhenDogExists_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long id = 1L;
        Dog existingDog = new Dog(id, "Labrador", "Max");

        when(exampleService.getDogById(id)).thenReturn(Optional.of(existingDog));
        doNothing().when(exampleService).deleteDog(id);

        // Act & Assert
        mockMvc.perform(delete("/v1/examples/dogs/{id}", id))
                .andExpect(status().isNoContent());

        verify(exampleService).deleteDog(id);
    }

    @Test
    public void deleteDog_WhenDogNotExists_ShouldReturn404() throws Exception {
        // Arrange
        Long id = 1L;
        when(exampleService.getDogById(id)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/v1/examples/dogs/{id}", id))
                .andExpect(status().isNotFound());

        verify(exampleService, never()).deleteDog(id);
    }
}