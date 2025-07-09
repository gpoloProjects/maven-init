package com.example.demo.models;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DogMapper {

    DogResponseDto toDogResponseDto(Dog dog);

    List<DogResponseDto> toDogResponseDtoList(List<Dog> dogs);

    Dog toDogEntity(DogRequestDto dogRequestDto);

    void updateDogEntity(@MappingTarget Dog dog, DogRequestDto dogRequestDto);
}
