package gr.hua.dit.petcare.core.service.model;

import gr.hua.dit.petcare.core.model.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Data required to create a new user
public record CreateUserRequest(
    @NotNull @NotBlank @Size(max = 50) String username,
    @NotNull @NotBlank @Size(max = 100) @Email String email,
    @NotNull @NotBlank String password,
    @NotNull @NotBlank String firstName,
    @NotNull @NotBlank String lastName,
    @NotNull UserType userType
) {}