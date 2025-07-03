package com.example.demo.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Dog response")
public class DogResponseDto {
    @Schema(description = "Unique identifier")
    private String id;

    @Schema(example = "Golden Retriever")
    private String breed;

    @Schema(example = "Buddy")
    private String name;
}