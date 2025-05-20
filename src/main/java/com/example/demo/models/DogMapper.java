package com.example.demo.models;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DogMapper {
    @Mapping(target = "id", ignore = true)
    Dog toDogEntity(DogRequestDto request);

    DogResponseDto toDogResponseDto(Dog entity);

    @Mapping(target = "id", ignore = true)
    List<DogResponseDto> toDogResponseDtoList(List<Dog> entities);

    void updateDogEntity(@MappingTarget Dog target, DogRequestDto source);
}