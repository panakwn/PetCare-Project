package gr.hua.dit.petcare.core.service;

import gr.hua.dit.petcare.core.model.Appointment;
import gr.hua.dit.petcare.core.service.model.ScheduleAppointmentRequest;
import java.util.List;

public interface AppointmentBusinessLogicService {
    String scheduleAppointment(ScheduleAppointmentRequest request, String username);
    void completeAppointment(Long appointmentId);
    void cancelAppointment(Long appointmentId);

    List<Appointment> getPetHistory(Long petId, String username);
}