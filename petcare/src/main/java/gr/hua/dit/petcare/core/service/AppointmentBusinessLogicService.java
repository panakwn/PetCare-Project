package gr.hua.dit.petcare.core.service;

import gr.hua.dit.petcare.core.service.model.ScheduleAppointmentRequest;

// Business logic operations for appointment lifecycle management
public interface AppointmentBusinessLogicService {
    String scheduleAppointment(ScheduleAppointmentRequest request);
    void completeAppointment(Long appointmentId);
    void cancelAppointment(Long appointmentId);
}