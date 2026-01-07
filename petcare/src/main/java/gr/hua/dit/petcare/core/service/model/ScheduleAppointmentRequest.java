package gr.hua.dit.petcare.core.service.model;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class ScheduleAppointmentRequest {

    @NotNull(message = "Πρέπει να επιλέξετε κατοικίδιο")
    private Long petId;

    @NotNull(message = "Πρέπει να επιλέξετε κτηνίατρο")
    private Long vetId;

    @NotNull(message = "Η ημερομηνία είναι υποχρεωτική")
    @Future(message = "Το ραντεβού πρέπει να είναι σε μελλοντική ημερομηνία")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime date;

    @Size(max = 255, message = "Η περιγραφή δεν μπορεί να ξεπερνά τους 255 χαρακτήρες")
    private String description;

    public ScheduleAppointmentRequest() {
    }

    public ScheduleAppointmentRequest(Long petId, Long vetId, LocalDateTime date, String description) {
        this.petId = petId;
        this.vetId = vetId;
        this.date = date;
        this.description = description;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public Long getVetId() {
        return vetId;
    }

    public void setVetId(Long vetId) {
        this.vetId = vetId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}