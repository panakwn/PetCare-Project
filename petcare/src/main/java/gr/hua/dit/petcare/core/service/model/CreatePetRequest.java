package gr.hua.dit.petcare.core.service.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Request DTO for pet registration with validation rules
public record CreatePetRequest(
    @NotBlank(message = "Enter a name")
    @Size(max = 50)
    String name,

    @NotBlank(message = "Enter animal type")
    String animalType,

    @Size(max = 50)
    String breed, 

    @NotNull(message = "Enter birthday")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthDate,

    Double weight
) {}