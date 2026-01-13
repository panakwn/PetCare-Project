package gr.hua.dit.petcare.core.service;

import java.util.List;

import gr.hua.dit.petcare.core.service.model.CreateUserRequest;
import gr.hua.dit.petcare.core.service.model.UserView;

// Service for user registration and retrieval operations
public interface UserService {
    
    UserView registerOwner(CreateUserRequest request);

    UserView findByUsername(String username);

    List<UserView> findAllVets();
    
    UserView findById(Long userId);
}