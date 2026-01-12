package gr.hua.dit.petcare.web.rest;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gr.hua.dit.petcare.core.repository.AppointmentRepository;
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

    public AppointmentRestController(AppointmentBusinessLogicService appointmentService,
                                     AppointmentRepository appointmentRepository,
                                     AppointmentMapper appointmentMapper) {
        this.appointmentService = appointmentService;
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
    }

    @GetMapping
    @Operation(summary = "List all appointments", description = "Returns a list of all appointments in the system")
    public List<AppointmentView> getAppointments() {
        return appointmentRepository.findAll().stream()
                .map(appointmentMapper::toView)
                .collect(Collectors.toList());
    }

    @PostMapping
    @Operation(summary = "Schedule a new appointment", description = "Creates a new appointment and notifies the owner")
    public ResponseEntity<Map<String, String>> createAppointment(@Valid @RequestBody ScheduleAppointmentRequest request) {
        String result = appointmentService.scheduleAppointment(request);
        return ResponseEntity.ok(Collections.singletonMap("message", result));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel an appointment", description = "Cancels an existing appointment (Vet Only)")
    @Secured("ROLE_VETERINARIAN")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok("Appointment cancelled successfully!");
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete an appointment", description = "Marks an appointment as completed (Vet Only)")
    @Secured("ROLE_VETERINARIAN")
    public ResponseEntity<String> completeAppointment(@PathVariable Long id) {
        appointmentService.completeAppointment(id);
        return ResponseEntity.ok("Appointment completed successfully!");
    }
}