package gr.hua.dit.petcare.core.service.impl;

import java.time.LocalDate;
import java.time.Period;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import gr.hua.dit.petcare.core.model.Pet;
import gr.hua.dit.petcare.core.model.User;
import gr.hua.dit.petcare.core.repository.PetRepository;
import gr.hua.dit.petcare.core.repository.UserRepository;
import gr.hua.dit.petcare.core.service.PetBusinessLogicService;
import gr.hua.dit.petcare.core.service.model.CreatePetRequest;

@Service
@Transactional
public class PetBusinessLogicServiceImpl implements PetBusinessLogicService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public PetBusinessLogicServiceImpl(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createPet(CreatePetRequest request, String ownerUsername) {
        User owner = userRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new RuntimeException("User not found: " + ownerUsername));

        Pet pet = new Pet();
        pet.setName(request.name());
        pet.setSpecies(request.animalType());
        pet.setBreed(request.breed());

        if (request.birthDate() != null) {
            int calculatedAge = Period.between(request.birthDate(), LocalDate.now()).getYears();
            pet.setAge(calculatedAge);
        } else {
            pet.setAge(0);
        }

        pet.setOwner(owner);
        petRepository.save(pet);
    }

    @Override
    public void deletePet(Long petId, String username) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + petId));

        if (!pet.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("You do not have permission to delete this pet!");
        }

        petRepository.delete(pet);
    }

    @Override
    public void updateVetNotes(Long petId, String notes) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + petId));

        pet.setVetNotes(notes);
        petRepository.save(pet);
    }
}