package com.example.demo.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dogs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dog entity")
public class Dog {
    @Id
    @Schema(description = "Unique identifier")
    private String id;

    @Version
    @Schema(description = "Version for optimistic locking")
    private Integer version;

    @NotBlank(message = "Breed is required")
    @Size(min = 2, max = 50, message = "Breed must be between 2 and 50 characters")
    @Schema(example = "Golden Retriever")
    private String breed;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
    @Schema(example = "Buddy")
    private String name;
}