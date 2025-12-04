package gr.hua.dit.petcare.core.service;

import gr.hua.dit.petcare.core.service.model.AppointmentView; 
import gr.hua.dit.petcare.core.service.model.ScheduleAppointmentRequest; 
import java.util.List;

public interface AppointmentService {
    
    AppointmentView scheduleAppointment(Long ownerId, ScheduleAppointmentRequest request);

    
    void cancelAppointment(Long appointmentId, Long userId); 

    
    List<AppointmentView> findVetAppointments(Long vetId);
    
    
    List<AppointmentView> findPetHistory(Long petId);
}