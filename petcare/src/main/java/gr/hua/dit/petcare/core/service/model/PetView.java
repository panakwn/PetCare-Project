package gr.hua.dit.petcare.core.service.model;

public record PetView(
    Long id,
    String name,
    String species,
    String breed,
    Integer age,
    UserView owner 
) {}