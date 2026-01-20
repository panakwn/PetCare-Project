package gr.hua.dit.petcare.web.ui;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import gr.hua.dit.petcare.core.model.Pet;
import gr.hua.dit.petcare.core.model.User;
import gr.hua.dit.petcare.core.repository.PetRepository;
import gr.hua.dit.petcare.core.repository.UserRepository;
import gr.hua.dit.petcare.core.service.AppointmentBusinessLogicService;
import gr.hua.dit.petcare.core.service.PetBusinessLogicService;
import gr.hua.dit.petcare.core.service.model.CreatePetRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/pets")
public class PetController {

    private final PetBusinessLogicService petService;
    private final AppointmentBusinessLogicService appointmentService; // Inject AppointmentService
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    public PetController(PetBusinessLogicService petService,
                         AppointmentBusinessLogicService appointmentService,
                         UserRepository userRepository,
                         PetRepository petRepository) {
        this.petService = petService;
        this.appointmentService = appointmentService;
        this.userRepository = userRepository;
        this.petRepository = petRepository;
    }

    @GetMapping
    public String listMyPets(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        model.addAttribute("pets", user.getPets());
        return "pets";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("createPetRequest", new CreatePetRequest(null, null, null, null, null));
        return "pet_new";
    }

    @PostMapping("/new")
    public String createPet(@Valid @ModelAttribute("createPetRequest") CreatePetRequest request,
                            BindingResult bindingResult,
                            @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            return "pet_new";
        }

        petService.createPet(request, userDetails.getUsername());
        return "redirect:/pets";
    }

    @PostMapping("/delete/{id}")
    public String deletePet(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        petService.deletePet(id, userDetails.getUsername());
        return "redirect:/pets";
    }

    @PostMapping("/{id}/notes")
    public String updateVetNotes(@PathVariable Long id, @RequestParam("notes") String notes) {
        petService.updateVetNotes(id, notes);
        return "redirect:/appointments";
    }

    @GetMapping("/{id}/history")
    public String viewPetHistory(@PathVariable Long id,
                                 Model model,
                                 @AuthenticationPrincipal UserDetails userDetails) {

        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        if (!pet.getOwner().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/pets";
        }

        model.addAttribute("pet", pet);
        model.addAttribute("appointments", appointmentService.getPetHistory(id, userDetails.getUsername()));

        return "pet_history";
    }
}