package gr.hua.dit.petcare.core.service.mapper;

import gr.hua.dit.petcare.core.model.User;
import gr.hua.dit.petcare.core.service.model.UserView;
import org.springframework.stereotype.Component;

// Maps User entity to UserView DTO
@Component
public class UserMapper {

    // Converts User entity to UserView object
    public UserView toView(User user) {
        if (user == null) return null;

        // Return new View object
        return new UserView(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getUserType()
        );
    }
}