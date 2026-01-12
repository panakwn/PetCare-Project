package gr.hua.dit.petcare.core.service.impl;

import gr.hua.dit.petcare.core.model.Appointment;
import gr.hua.dit.petcare.core.model.Pet;
import gr.hua.dit.petcare.core.model.User;
import gr.hua.dit.petcare.core.port.EmailNotificationPort;
import gr.hua.dit.petcare.core.repository.AppointmentRepository;
import gr.hua.dit.petcare.core.repository.PetRepository;
import gr.hua.dit.petcare.core.repository.UserRepository;
import gr.hua.dit.petcare.core.service.AppointmentBusinessLogicService;
import gr.hua.dit.petcare.core.service.model.ScheduleAppointmentRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class AppointmentBusinessLogicServiceImpl implements AppointmentBusinessLogicService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final EmailNotificationPort emailPort;

    public AppointmentBusinessLogicServiceImpl(AppointmentRepository appointmentRepository,
                                               UserRepository userRepository,
                                               PetRepository petRepository,
                                               EmailNotificationPort emailPort) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.emailPort = emailPort;
    }

    @Override
    public String scheduleAppointment(ScheduleAppointmentRequest request) {
        Pet pet = petRepository.findById(request.getPetId())
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + request.getPetId()));

        User owner = pet.getOwner();
        if (owner == null) {
            throw new RuntimeException("Owner not found for this pet");
        }

        User vet = userRepository.findById(request.getVetId())
                .orElseThrow(() -> new RuntimeException("Vet not found with id: " + request.getVetId()));

        LocalDateTime requestedDate = request.getDate();
        LocalDateTime startBound = requestedDate.minusDays(2);
        LocalDateTime endBound = requestedDate.plusDays(2);

        boolean conflictExists = appointmentRepository.existsByPetIdAndDateBetween(
                pet.getId(), startBound, endBound
        );

        if (conflictExists) {
            throw new RuntimeException("New appointment not allowed. There must be a gap of 2 days from a previous or next appointment for the same pet.");
        }

        LocalDateTime newAppointmentStart = request.getDate();
        LocalDateTime newAppointmentEnd = newAppointmentStart.plusMinutes(30);

        boolean isVetBusy = appointmentRepository.existsOverlappingAppointment(
                vet.getId(),
                newAppointmentStart,
                newAppointmentEnd
        );

        if (isVetBusy) {
            throw new RuntimeException("The veterinarian is not available at the selected time (" + newAppointmentStart + " - " + newAppointmentEnd + "). Please select another time.");
        }

        Appointment appointment = new Appointment();
        appointment.setPet(pet);
        appointment.setVet(vet);
        appointment.setDate(requestedDate);
        appointment.setDescription(request.getDescription());
        appointment.setStatus("SCHEDULED");
        appointment.setStartTime(newAppointmentStart);
        appointment.setEndTime(newAppointmentEnd);

        appointmentRepository.save(appointment);

        emailPort.sendEmail(
                owner.getEmail(),
                "Appointment Confirmation",
                "Hello " + owner.getFirstName() + ", the appointment for your pet (" + pet.getName() + ") has been successfully registered!"
        );

        return "Appointment created successfully with ID: " + appointment.getId();
    }

    @Override
    public void completeAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus("COMPLETED");
        appointmentRepository.save(appointment);
    }

    @Override
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus("CANCELLED");
        appointmentRepository.save(appointment);
    }
}