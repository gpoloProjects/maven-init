package com.example.demo.repositories;

import com.example.demo.models.Dog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogRepository extends MongoRepository<Dog, String> {
}

