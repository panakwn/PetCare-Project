package gr.hua.dit.petcare.web.rest;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// REST controller for pet management endpoints
@RestController
@RequestMapping("/api/pets")
@Tag(name = "Pets", description = "API for managing pets")
@SecurityRequirement(name = "bearer-key") // Required so JWT auth works in Swagger UI
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

    // Returns pets for the authenticated user
    @Operation(summary = "List Pets", description = "Returns pets of the authenticated user")
    @GetMapping
    public ResponseEntity<List<PetView>> getMyPets(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<PetView> pets = user.getPets().stream()
                .map(petMapper::toView)
                .collect(Collectors.toList());

        return ResponseEntity.ok(pets);
    }

    // Creates a pet for the current user
    @Operation(summary = "Create Pet", description = "Adds a new pet for the authenticated user")
    @PostMapping
    @Secured("ROLE_PET_OWNER")
    public ResponseEntity<String> createPet(@Valid @RequestBody CreatePetRequest request,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        petService.createPet(request, userDetails.getUsername());
        return ResponseEntity.ok("Pet created successfully!");
    }

    // Deletes a pet owned by the current user
    @Operation(summary = "Delete Pet", description = "Deletes a pet (and its appointments) by ID")
    @DeleteMapping("/{id}")
    @Secured("ROLE_PET_OWNER")
    public ResponseEntity<String> deletePet(@PathVariable Long id,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        petService.deletePet(id, userDetails.getUsername());
        return ResponseEntity.ok("Pet deleted successfully!");
    }

    // Updates veterinary notes for a pet (vets only)
    @Operation(summary = "Update Vet Notes", description = "Updates veterinary notes for a pet (Veterinarians only)")
    @PostMapping("/{id}/notes")
    @Secured("ROLE_VETERINARIAN")
    public ResponseEntity<String> updateVetNotes(@PathVariable Long id,
                                                 @RequestParam("notes") String notes) {
        petService.updateVetNotes(id, notes);
        return ResponseEntity.ok("Notes updated successfully!");
    }
}