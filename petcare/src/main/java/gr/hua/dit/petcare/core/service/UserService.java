package gr.hua.dit.petcare.core.service;

import gr.hua.dit.petcare.core.service.model.CreateUserRequest;
import gr.hua.dit.petcare.core.service.model.UserView;
import java.util.List;

// Service interface for User related operations
public interface UserService {
    
    // Registers a new pet owner or vet
    UserView registerOwner(CreateUserRequest request);

    // Finds a user by their username
    UserView findByUsername(String username);

    List<UserView> findAllVets();
    
    // Finds a user by their ID
    UserView findById(Long userId);
}