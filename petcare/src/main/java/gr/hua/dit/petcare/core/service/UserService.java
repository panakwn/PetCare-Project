package gr.hua.dit.petcare.core.service;

import gr.hua.dit.petcare.core.service.model.CreateUserRequest;
import gr.hua.dit.petcare.core.service.model.UserView;
import java.util.List;

public interface UserService {
    
    UserView registerOwner(CreateUserRequest request);

    
    UserView findByUsername(String username);

    List<UserView> findAllVets();
    
    UserView findById(Long userId);
}