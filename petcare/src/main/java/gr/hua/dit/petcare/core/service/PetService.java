package gr.hua.dit.petcare.core.service;

import gr.hua.dit.petcare.core.service.model.CreatePetRequest; 
import gr.hua.dit.petcare.core.service.model.PetView; 
import java.util.List;

public interface PetService {
    PetView createPet(Long ownerId, CreatePetRequest request);

    List<PetView> findOwnersPets(Long ownerId);

    PetView getPetDetails(Long petId, Long ownerId);
}