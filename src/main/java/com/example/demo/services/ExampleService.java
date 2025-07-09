package com.example.demo.services;

import com.example.demo.models.Dog;
import com.example.demo.models.DogMapper;
import com.example.demo.models.DogRequestDto;
import com.example.demo.models.DogResponseDto;
import com.example.demo.repositories.DogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExampleService {

    private final DogRepository dogRepository;
    private final DogMapper dogMapper;

    // DTO-based methods for controller layer
    public List<DogResponseDto> getAllDogsAsDto() {
        log.info("Fetching all dogs from the repository");
        List<Dog> dogs = dogRepository.findAll();
        return dogMapper.toDogResponseDtoList(dogs);
    }

    public DogResponseDto getDogByIdAsDto(String id) {
        log.info("Fetching dog with ID: {}", id);
        Dog dog = dogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Dog not found with id: " + id));
        return dogMapper.toDogResponseDto(dog);
    }

    public DogResponseDto createDogFromDto(DogRequestDto requestDto) {
        log.info("Creating a new dog from DTO: {}", requestDto);
        Dog dog = dogMapper.toDogEntity(requestDto);
        Dog savedDog = dogRepository.save(dog);
        return dogMapper.toDogResponseDto(savedDog);
    }

    public DogResponseDto updateDogFromDto(String id, DogRequestDto requestDto) {
        log.info("Updating dog with ID: {} using DTO: {}", id, requestDto);
        Dog existingDog = dogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Dog not found with id: " + id));

        dogMapper.updateDogEntity(existingDog, requestDto);
        existingDog.setId(id); // Ensure the ID is preserved
        Dog updatedDog = dogRepository.save(existingDog);

        return dogMapper.toDogResponseDto(updatedDog);
    }

    public void deleteDogById(String id) {
        log.info("Deleting dog with ID: {}", id);
        // Check if dog exists before attempting deletion
        if (!dogRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Dog not found with id: " + id);
        }
        dogRepository.deleteById(id);
    }

    // Legacy entity-based methods (kept for backward compatibility)
    public List<Dog> getAllDogs() {
        log.info("Fetching all dogs from the repository");
        return dogRepository.findAll();
    }

    public Optional<Dog> getDogById(String id) {
        log.info("Fetching dog with ID: {}", id);
        return dogRepository.findById(id);
    }

    public Dog createDog(Dog dog) {
        log.info("Creating a new dog: {}", dog);
        return dogRepository.save(dog);
    }

    public void deleteDog(String id) {
        log.info("Deleting dog with ID: {}", id);
        dogRepository.deleteById(id);
    }
}