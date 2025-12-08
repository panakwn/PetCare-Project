package gr.hua.dit.petcare.core.service.model;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ScheduleAppointmentRequest(
    @NotNull Long petId,
    @NotNull Long vetId,
    @NotNull LocalDateTime date,
    String description
) {}