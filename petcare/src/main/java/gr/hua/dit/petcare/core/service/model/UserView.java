package gr.hua.dit.petcare.core.service.model;

import gr.hua.dit.petcare.core.model.UserType;

// A read-only view of User data (DTO)
public record UserView(
    Long id,
    String username,
    String email,
    String firstName,
    String lastName,
    UserType userType
) {
    // Returns the full name of the user
    public String fullName() {
        return firstName + " " + lastName;
    }
}