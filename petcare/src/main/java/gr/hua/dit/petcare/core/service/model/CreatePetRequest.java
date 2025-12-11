package gr.hua.dit.petcare.core.service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Data required to create a new pet
public record CreatePetRequest(
    @NotNull @NotBlank String name,
    @NotNull @NotBlank String species,
    String breed,
    @NotNull Integer age
) {}