package gr.hua.dit.petcare.core.service.model;

import java.time.LocalDateTime;

// A read-only view of Appointment data (DTO)
public record AppointmentView(
    Long id,
    LocalDateTime date,
    String description,
    String status,
    PetView pet,
    UserView vet
) {}