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
        Dog dog1 = new Dog("1", "Labrador", "Max");
        Dog dog2 = new Dog("2", "Beagle", "Charlie");
        List<Dog> dogs = Arrays.asList(dog1, dog2);

        DogResponseDto dto1 = new DogResponseDto();
        dto1.setId("1");
        dto1.setBreed("Labrador");
        dto1.setName("Max");

        DogResponseDto dto2 = new DogResponseDto();
        dto2.setId("2");
        dto2.setBreed("Beagle");
        dto2.setName("Charlie");

        List<DogResponseDto> dogDtos = Arrays.asList(dto1, dto2);

        when(exampleService.getAllDogs()).thenReturn(dogs);
        when(dogMapper.toDogResponseDtoList(dogs)).thenReturn(dogDtos);

        // Act & Assert
        mockMvc.perform(get("/v1/examples/dogs"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dogDtos)));

        verify(exampleService, times(1)).getAllDogs();
    }

    @Test
    public void getDogById_ShouldReturnDog() throws Exception {
        // Arrange
        Dog dog = new Dog("1", "Labrador", "Max");
        DogResponseDto dto = new DogResponseDto();
        dto.setId("1");
        dto.setBreed("Labrador");
        dto.setName("Max");

        when(exampleService.getDogById("1")).thenReturn(Optional.of(dog));
        when(dogMapper.toDogResponseDto(dog)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/v1/examples/dogs/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));

        verify(exampleService, times(1)).getDogById("1");
    }

    @Test
    public void getDogById_NotFound_ShouldReturn404() throws Exception {
        // Arrange
        when(exampleService.getDogById("999")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/v1/examples/dogs/999"))
                .andExpect(status().isNotFound());

        verify(exampleService, times(1)).getDogById("999");
    }

    @Test
    public void createDog_ShouldReturnCreatedDog() throws Exception {
        // Arrange
        DogRequestDto requestDto = new DogRequestDto();
        requestDto.setBreed("Bulldog");
        requestDto.setName("Rocky");

        Dog unsavedDog = new Dog(null, "Bulldog", "Rocky");
        Dog savedDog = new Dog("3", "Bulldog", "Rocky");
        DogResponseDto responseDto = new DogResponseDto();
        responseDto.setId("3");
        responseDto.setBreed("Bulldog");
        responseDto.setName("Rocky");

        when(dogMapper.toDogEntity(requestDto)).thenReturn(unsavedDog);
        when(exampleService.createDog(unsavedDog)).thenReturn(savedDog);
        when(dogMapper.toDogResponseDto(savedDog)).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/v1/examples/dogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));

        verify(exampleService, times(1)).createDog(any(Dog.class));
    }

    @Test
    public void updateDog_ShouldReturnUpdatedDog() throws Exception {
        // Arrange
        DogRequestDto requestDto = new DogRequestDto();
        requestDto.setBreed("Updated Breed");
        requestDto.setName("Updated Name");

        Dog existingDog = new Dog("1", "Labrador", "Max");
        Dog updatedDog = new Dog("1", "Updated Breed", "Updated Name");
        DogResponseDto responseDto = new DogResponseDto();
        responseDto.setId("1");
        responseDto.setBreed("Updated Breed");
        responseDto.setName("Updated Name");

        when(exampleService.getDogById("1")).thenReturn(Optional.of(existingDog));
        when(exampleService.createDog(any(Dog.class))).thenReturn(updatedDog);
        when(dogMapper.toDogResponseDto(updatedDog)).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(put("/v1/examples/dogs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));

        verify(exampleService, times(1)).getDogById("1");
        verify(dogMapper, times(1)).updateDogEntity(any(Dog.class), any(DogRequestDto.class));
    }

    @Test
    public void updateDog_NotFound_ShouldReturn404() throws Exception {
        // Arrange
        DogRequestDto requestDto = new DogRequestDto();
        requestDto.setBreed("Updated Breed");
        requestDto.setName("Updated Name");

        when(exampleService.getDogById("999")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/v1/examples/dogs/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());

        verify(exampleService, times(1)).getDogById("999");
        verify(exampleService, never()).createDog(any(Dog.class));
    }

    @Test
    public void deleteDog_ShouldReturnNoContent() throws Exception {
        // Arrange
        Dog dog = new Dog("1", "Labrador", "Max");
        when(exampleService.getDogById("1")).thenReturn(Optional.of(dog));

        // Act & Assert
        mockMvc.perform(delete("/v1/examples/dogs/1"))
                .andExpect(status().isNoContent());

        verify(exampleService, times(1)).getDogById("1");
        verify(exampleService, times(1)).deleteDog("1");
    }

    @Test
    public void deleteDog_NotFound_ShouldReturn404() throws Exception {
        // Arrange
        when(exampleService.getDogById("999")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/v1/examples/dogs/999"))
                .andExpect(status().isNotFound());

        verify(exampleService, times(1)).getDogById("999");
        verify(exampleService, never()).deleteDog(anyString());
    }
}