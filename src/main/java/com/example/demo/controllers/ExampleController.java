package com.example.demo.controllers;

import com.example.demo.models.Dog;
import com.example.demo.models.DogMapper;
import com.example.demo.models.DogRequestDto;
import com.example.demo.models.DogResponseDto;
import com.example.demo.services.ExampleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/v1/examples")
@RequiredArgsConstructor
@Tag(name = "Dog Management", description = "API endpoints for managing dogs")
public class ExampleController {

    private final ExampleService exampleService;
    private final DogMapper dogMapper;

    @GetMapping("/dogs")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all dogs", description = "Retrieves a list of all dogs")
    public List<DogResponseDto> getAllDogs() {
        List<Dog> dogs = exampleService.getAllDogs();
        return dogMapper.toDogResponseDtoList(dogs);
    }

    @GetMapping("/dogs/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get dog by ID", description = "Retrieves a specific dog")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dog found"),
            @ApiResponse(responseCode = "404", description = "Dog not found")
    })
    public DogResponseDto getDogById(@Parameter(description = "Dog ID") @PathVariable String id) {
        Dog dog = exampleService.getDogById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Dog not found with id: " + id));
        return dogMapper.toDogResponseDto(dog);
    }

    @PostMapping("/dogs")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new dog")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Dog created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public DogResponseDto createDog(@Valid @RequestBody DogRequestDto request) {
        Dog dogEntity = dogMapper.toDogEntity(request);
        Dog savedDog = exampleService.createDog(dogEntity);
        return dogMapper.toDogResponseDto(savedDog);
    }

    @PutMapping("/dogs/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an existing dog")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dog updated"),
            @ApiResponse(responseCode = "404", description = "Dog not found")
    })
    public DogResponseDto updateDog(@PathVariable String id, @Valid @RequestBody DogRequestDto request) {
        Dog existingDog = exampleService.getDogById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Dog not found with id: " + id));

        dogMapper.updateDogEntity(existingDog, request);
        existingDog.setId(id); // Ensure the ID is set
        Dog updatedDog = exampleService.createDog(existingDog);

        return dogMapper.toDogResponseDto(updatedDog);
    }

    @DeleteMapping("/dogs/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a dog")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Dog deleted"),
            @ApiResponse(responseCode = "404", description = "Dog not found")
    })
    public void deleteDog(@PathVariable String id) {
        if (!exampleService.getDogById(id).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Dog not found with id: " + id);
        }
        exampleService.deleteDog(id);
    }
}