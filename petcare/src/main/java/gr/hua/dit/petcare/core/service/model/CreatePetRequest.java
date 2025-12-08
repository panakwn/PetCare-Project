package gr.hua.dit.petcare.core.service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePetRequest(
    @NotNull @NotBlank String name,
    @NotNull @NotBlank String species,
    String breed,
    @NotNull Integer age
) {}