package com.example.demo.services;

import com.example.demo.models.Dog;
import com.example.demo.repositories.DogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExampleService {

    private final DogRepository dogRepository;

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