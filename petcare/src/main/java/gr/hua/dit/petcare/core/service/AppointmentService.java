package gr.hua.dit.petcare.core.service;

import gr.hua.dit.petcare.core.service.model.AppointmentView; 
import gr.hua.dit.petcare.core.service.model.ScheduleAppointmentRequest; 
import java.util.List;

// Service interface for Appointment related operations
public interface AppointmentService {
    
    AppointmentView scheduleAppointment(Long ownerId, ScheduleAppointmentRequest request);

    
    void cancelAppointment(Long appointmentId, Long userId); 

    
    List<AppointmentView> findVetAppointments(Long vetId);
    
    // Finds the appointment history for a specific pet
    List<AppointmentView> findPetHistory(Long petId);
}