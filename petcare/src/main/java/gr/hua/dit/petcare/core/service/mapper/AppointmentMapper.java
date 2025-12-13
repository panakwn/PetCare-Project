package gr.hua.dit.petcare.core.service.mapper;

import gr.hua.dit.petcare.core.model.Appointment;
import gr.hua.dit.petcare.core.service.model.AppointmentView;
import org.springframework.stereotype.Component;

// Maps Appointment entity to AppointmentView DTO
@Component
public class AppointmentMapper {

    
    private final PetMapper petMapper;
    private final UserMapper userMapper;

    public AppointmentMapper(PetMapper petMapper, UserMapper userMapper) {
        this.petMapper = petMapper;
        this.userMapper = userMapper;
    }

    // Converts Appointment entity to DTO
    public AppointmentView toView(Appointment appointment) {
     
        if (appointment == null) return null;

        // Map appointment details along with Pet and Vet
        return new AppointmentView(
            appointment.getId(),
            appointment.getDate(),
            appointment.getDescription(),
            appointment.getStatus(),
            petMapper.toView(appointment.getPet()), // Convert Pet
            userMapper.toView(appointment.getVet()) // Convert Vet
        );
    }
}