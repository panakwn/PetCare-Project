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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

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
    public String listAppointments(Model model) {
        model.addAttribute("appointments", appointmentRepository.findAll());
        return "appointments";
    }

    @GetMapping("/new")
    public String showScheduleForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("pets", currentUser.getPets());

        model.addAttribute("vets", userRepository.findByUserType(UserType.VETERINARIAN));

        return "appointment_new";
    }

    @PostMapping("/new")
    public String scheduleAppointment(@Valid @ModelAttribute ScheduleAppointmentRequest request,
                                      BindingResult bindingResult,
                                      Model model,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            User currentUser = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
            model.addAttribute("pets", currentUser.getPets());

            model.addAttribute("vets", userRepository.findByUserType(UserType.VETERINARIAN));

            return "appointment_new";
        }

        appointmentService.scheduleAppointment(request);
        return "redirect:/appointments";
    }
}