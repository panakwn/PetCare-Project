package gr.hua.dit.petcare.web.ui;

import gr.hua.dit.petcare.core.model.User;
import gr.hua.dit.petcare.core.model.UserType;
import gr.hua.dit.petcare.core.repository.AppointmentRepository;
import gr.hua.dit.petcare.core.repository.UserRepository;
import gr.hua.dit.petcare.core.service.AppointmentBusinessLogicService;
import gr.hua.dit.petcare.core.service.model.ScheduleAppointmentRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    /**
     * UI controller for appointment pages.
     * Handles listing, scheduling and updating appointment state via web views.
     */

    private final AppointmentBusinessLogicService appointmentService;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public AppointmentController(AppointmentBusinessLogicService appointmentService,
                                 AppointmentRepository appointmentRepository,
                                 UserRepository userRepository) {
        this.appointmentService = appointmentService;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listAppointments(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime now = LocalDateTime.now();

        if (currentUser.getUserType() == UserType.VETERINARIAN) {
            model.addAttribute("appointments", appointmentRepository.findActiveByVetId(currentUser.getId(), now));
            model.addAttribute("isVet", true);
        } else {
            model.addAttribute("appointments", appointmentRepository.findActiveByPetOwnerId(currentUser.getId(), now));
            model.addAttribute("isVet", false);
        }

        return "appointments";
    }

    @GetMapping("/new")
    public String showScheduleForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("pets", currentUser.getPets());
        model.addAttribute("vets", userRepository.findByUserType(UserType.VETERINARIAN));
        model.addAttribute("scheduleAppointmentRequest", new ScheduleAppointmentRequest());

        return "appointment_new";
    }

    @PostMapping("/new")
    public String scheduleAppointment(@Valid @ModelAttribute("scheduleAppointmentRequest") ScheduleAppointmentRequest request,
                                      BindingResult bindingResult,
                                      Model model,
                                      @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = userRepository.findByUsernameWithPets(userDetails.getUsername()).orElseThrow();

        if (bindingResult.hasErrors()) {
            model.addAttribute("pets", currentUser.getPets());
            model.addAttribute("vets", userRepository.findByUserType(UserType.VETERINARIAN));
            return "appointment_new";
        }

        try {
            appointmentService.scheduleAppointment(request);
        } catch (RuntimeException e) {
            bindingResult.addError(new ObjectError("scheduleAppointmentRequest", e.getMessage()));

            model.addAttribute("pets", currentUser.getPets());
            model.addAttribute("vets", userRepository.findByUserType(UserType.VETERINARIAN));

            return "appointment_new";
        }

        return "redirect:/appointments";
    }

    @PostMapping("/{id}/complete")
    public String completeAppointment(@PathVariable Long id) {
        appointmentService.completeAppointment(id);
        return "redirect:/appointments";
    }

    @PostMapping("/{id}/cancel")
    public String cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return "redirect:/appointments";
    }
}