package gr.hua.dit.petcare.core.service.model;

// DTO representing pet details with owner information
public record PetView(
    Long id,
    String name,
    String species,
    String breed,
    Integer age,
    UserView owner 
) {}