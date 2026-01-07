package gr.hua.dit.petcare.core.service;

import gr.hua.dit.petcare.core.service.model.CreatePetRequest;

public interface PetBusinessLogicService {
    void createPet(CreatePetRequest request, String ownerUsername);
}