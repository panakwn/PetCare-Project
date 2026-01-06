package gr.hua.dit.petcare.core.service;

import gr.hua.dit.petcare.core.service.model.ScheduleAppointmentRequest; 

public interface AppointmentBusinessLogicService {

    String scheduleAppointment(ScheduleAppointmentRequest request);
}