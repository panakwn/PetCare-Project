package gr.hua.dit.petcare.core.service.model;

// A read-only view of Pet data (DTO)
public record PetView(
    Long id,
    String name,
    String species,
    String breed,
    Integer age,
    UserView owner 
) {}