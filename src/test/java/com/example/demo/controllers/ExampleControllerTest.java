package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.services.ExampleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
        DogResponseDto dto1 = new DogResponseDto();
        dto1.setId("1");
        dto1.setBreed("Labrador");
        dto1.setName("Max");

        DogResponseDto dto2 = new DogResponseDto();
        dto2.setId("2");
        dto2.setBreed("Beagle");
        dto2.setName("Charlie");

        List<DogResponseDto> dogDtos = Arrays.asList(dto1, dto2);

        when(exampleService.getAllDogsAsDto()).thenReturn(dogDtos);

        // Act & Assert
        mockMvc.perform(get("/v1/examples/dogs"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dogDtos)));

        verify(exampleService, times(1)).getAllDogsAsDto();
    }

    @Test
    public void getDogById_ShouldReturnDog() throws Exception {
        // Arrange
        DogResponseDto dto = new DogResponseDto();
        dto.setId("1");
        dto.setBreed("Labrador");
        dto.setName("Max");

        when(exampleService.getDogByIdAsDto("1")).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/v1/examples/dogs/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));

        verify(exampleService, times(1)).getDogByIdAsDto("1");
    }

    @Test
    public void getDogById_NotFound_ShouldReturn404() throws Exception {
        // Arrange
        when(exampleService.getDogByIdAsDto("999")).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Dog not found with id: 999"));

        // Act & Assert
        mockMvc.perform(get("/v1/examples/dogs/999"))
                .andExpect(status().isNotFound());

        verify(exampleService, times(1)).getDogByIdAsDto("999");
    }

    @Test
    public void createDog_ShouldReturnCreatedDog() throws Exception {
        // Arrange
        DogRequestDto requestDto = new DogRequestDto();
        requestDto.setBreed("Bulldog");
        requestDto.setName("Rocky");

        DogResponseDto responseDto = new DogResponseDto();
        responseDto.setId("3");
        responseDto.setBreed("Bulldog");
        responseDto.setName("Rocky");

        when(exampleService.createDogFromDto(any(DogRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/v1/examples/dogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));

        verify(exampleService, times(1)).createDogFromDto(any(DogRequestDto.class));
    }

    @Test
    public void updateDog_ShouldReturnUpdatedDog() throws Exception {
        // Arrange
        DogRequestDto requestDto = new DogRequestDto();
        requestDto.setBreed("Updated Breed");
        requestDto.setName("Updated Name");

        DogResponseDto responseDto = new DogResponseDto();
        responseDto.setId("1");
        responseDto.setBreed("Updated Breed");
        responseDto.setName("Updated Name");

        when(exampleService.updateDogFromDto(eq("1"), any(DogRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(put("/v1/examples/dogs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));

        verify(exampleService, times(1)).updateDogFromDto(eq("1"), any(DogRequestDto.class));
    }

    @Test
    public void updateDog_NotFound_ShouldReturn404() throws Exception {
        // Arrange
        DogRequestDto requestDto = new DogRequestDto();
        requestDto.setBreed("Updated Breed");
        requestDto.setName("Updated Name");

        when(exampleService.updateDogFromDto(eq("999"), any(DogRequestDto.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Dog not found with id: 999"));

        // Act & Assert
        mockMvc.perform(put("/v1/examples/dogs/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());

        verify(exampleService, times(1)).updateDogFromDto(eq("999"), any(DogRequestDto.class));
    }

    @Test
    public void deleteDog_ShouldReturnNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/v1/examples/dogs/1"))
                .andExpect(status().isNoContent());

        verify(exampleService, times(1)).deleteDogById("1");
    }

    @Test
    public void deleteDog_NotFound_ShouldReturn404() throws Exception {
        // Arrange
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Dog not found with id: 999"))
                .when(exampleService).deleteDogById("999");

        // Act & Assert
        mockMvc.perform(delete("/v1/examples/dogs/999"))
                .andExpect(status().isNotFound());

        verify(exampleService, times(1)).deleteDogById("999");
    }
}