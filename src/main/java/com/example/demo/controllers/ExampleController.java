package com.example.demo.controllers;

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

import java.util.List;

@RestController
@RequestMapping("/v1/examples")
@RequiredArgsConstructor
@Tag(name = "Dog Management", description = "API endpoints for managing dogs")
public class ExampleController {

    private final ExampleService exampleService;

    @GetMapping("/dogs")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all dogs", description = "Retrieves a list of all dogs")
    public List<DogResponseDto> getAllDogs() {
        return exampleService.getAllDogsAsDto();
    }

    @GetMapping("/dogs/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get dog by ID", description = "Retrieves a specific dog")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dog found"),
            @ApiResponse(responseCode = "404", description = "Dog not found")
    })
    public DogResponseDto getDogById(@Parameter(description = "Dog ID") @PathVariable String id) {
        return exampleService.getDogByIdAsDto(id);
    }

    @PostMapping("/dogs")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new dog")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Dog created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public DogResponseDto createDog(@Valid @RequestBody DogRequestDto request) {
        return exampleService.createDogFromDto(request);
    }

    @PutMapping("/dogs/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an existing dog")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dog updated"),
            @ApiResponse(responseCode = "404", description = "Dog not found")
    })
    public DogResponseDto updateDog(@PathVariable String id, @Valid @RequestBody DogRequestDto request) {
        return exampleService.updateDogFromDto(id, request);
    }

    @DeleteMapping("/dogs/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a dog")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Dog deleted"),
            @ApiResponse(responseCode = "404", description = "Dog not found")
    })
    public void deleteDog(@PathVariable String id) {
        exampleService.deleteDogById(id);
    }
}