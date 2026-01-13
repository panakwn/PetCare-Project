package gr.hua.dit.petcare.web.rest;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import gr.hua.dit.petcare.core.model.Appointment;
import gr.hua.dit.petcare.core.model.User;
import gr.hua.dit.petcare.core.model.UserType;
import gr.hua.dit.petcare.core.repository.AppointmentRepository;
import gr.hua.dit.petcare.core.repository.UserRepository;
import gr.hua.dit.petcare.core.service.AppointmentBusinessLogicService;
import gr.hua.dit.petcare.core.service.mapper.AppointmentMapper;
import gr.hua.dit.petcare.core.service.model.AppointmentView;
import gr.hua.dit.petcare.core.service.model.ScheduleAppointmentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointments", description = "API for managing appointments")
@SecurityRequirement(name = "bearer-key")
public class AppointmentRestController {

    private final AppointmentBusinessLogicService appointmentService;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final UserRepository userRepository;

    public AppointmentRestController(AppointmentBusinessLogicService appointmentService,
                                     AppointmentRepository appointmentRepository,
                                     AppointmentMapper appointmentMapper,
                                     UserRepository userRepository) {
        this.appointmentService = appointmentService;
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.userRepository = userRepository;
    }

    @GetMapping
    @Operation(summary = "List my appointments", description = "Returns a list of appointments relevant to the current user (Vet or Owner)")
    public List<AppointmentView> getAppointments(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<Appointment> appointments;

        if (currentUser.getUserType() == UserType.VETERINARIAN) {
            appointments = appointmentRepository.findByVet(currentUser);
        } else {
            appointments = appointmentRepository.findByPet_Owner(currentUser);
        }

        return appointments.stream()
                .map(appointmentMapper::toView)
                .collect(Collectors.toList());
    }

    @PostMapping
    @Operation(summary = "Schedule a new appointment", description = "Creates a new appointment and notifies the owner")
    @Secured("ROLE_PET_OWNER")
    public ResponseEntity<Map<String, String>> createAppointment(@Valid @RequestBody ScheduleAppointmentRequest request,
                                                                 @AuthenticationPrincipal UserDetails userDetails) { // <--- Προσθήκη userDetails
       
        // Περνάμε το username στο Service για έλεγχο
        String result = appointmentService.scheduleAppointment(request, userDetails.getUsername());
       
        return ResponseEntity.ok(Collections.singletonMap("message", result));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel an appointment", description = "Cancels an existing appointment (Must be the assigned Vet)")
    @Secured("ROLE_VETERINARIAN")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        checkAppointmentOwnership(id, userDetails.getUsername());
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok("Appointment cancelled successfully!");
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete an appointment", description = "Marks an appointment as completed (Must be the assigned Vet)")
    @Secured("ROLE_VETERINARIAN")
    public ResponseEntity<String> completeAppointment(@PathVariable Long id,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        checkAppointmentOwnership(id, userDetails.getUsername());
        appointmentService.completeAppointment(id);
        return ResponseEntity.ok("Appointment completed successfully!");
    }

    private void checkAppointmentOwnership(Long appointmentId, String username) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));

        if (!appointment.getVet().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: You are not assigned to this appointment.");
        }
    }
}