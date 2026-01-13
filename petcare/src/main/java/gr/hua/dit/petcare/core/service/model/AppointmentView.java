package gr.hua.dit.petcare.core.service.model;

import java.time.LocalDateTime;

// DTO representing appointment details with pet and veterinarian information
public record AppointmentView(
    Long id,
    LocalDateTime date,
    String description,
    String status,
    PetView pet,
    UserView vet
) {}