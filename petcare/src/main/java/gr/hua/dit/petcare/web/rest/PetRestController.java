package gr.hua.dit.petcare.web.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gr.hua.dit.petcare.core.model.User;
import gr.hua.dit.petcare.core.repository.UserRepository;
import gr.hua.dit.petcare.core.service.PetBusinessLogicService;
import gr.hua.dit.petcare.core.service.mapper.PetMapper;
import gr.hua.dit.petcare.core.service.model.CreatePetRequest;
import gr.hua.dit.petcare.core.service.model.PetView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pets")
@Tag(name = "Pets", description = "API for managing pets")
@SecurityRequirement(name = "bearer-key")
/**
 * REST endpoints for pet management.
 * Allows owners to list, create and delete their pets.
 */
public class PetRestController {

    private final PetBusinessLogicService petService;
    private final UserRepository userRepository;
    private final PetMapper petMapper;

    public PetRestController(PetBusinessLogicService petService,
                             UserRepository userRepository,
                             PetMapper petMapper) {
        this.petService = petService;
        this.userRepository = userRepository;
        this.petMapper = petMapper;
    }

    @Operation(summary = "Pet List", description = "Returns the pets of the logged-in user")
    @GetMapping
    public ResponseEntity<List<PetView>> getMyPets(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<PetView> pets = user.getPets().stream()
                .map(petMapper::toView)
                .collect(Collectors.toList());

        return ResponseEntity.ok(pets);
    }

    @Operation(summary = "Create Pet", description = "Adds a new pet to the user")
    @PostMapping
    @Secured("ROLE_OWNER")
    public ResponseEntity<String> createPet(@Valid @RequestBody CreatePetRequest request,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        petService.createPet(request, userDetails.getUsername());
        return ResponseEntity.ok("Pet created successfully!");
    }

    @Operation(summary = "Delete Pet", description = "Deletes a pet (and its appointments) by ID")
    @DeleteMapping("/{id}")
    @Secured("ROLE_OWNER")
    public ResponseEntity<String> deletePet(@PathVariable Long id,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        petService.deletePet(id, userDetails.getUsername());
        return ResponseEntity.ok("Pet deleted successfully!");
    }

    @Operation(summary = "Update Veterinary Notes", description = "Updates the medical notes of a pet (Only for Veterinarians)")
    @PostMapping("/{id}/notes")
    @Secured("ROLE_VETERINARIAN")
    public ResponseEntity<String> updateVetNotes(@PathVariable Long id,
                                                 @RequestParam("notes") String notes) {
        petService.updateVetNotes(id, notes);
        return ResponseEntity.ok("Notes updated successfully!");
    }
}