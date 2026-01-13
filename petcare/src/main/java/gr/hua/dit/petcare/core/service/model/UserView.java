package gr.hua.dit.petcare.core.service.model;

import gr.hua.dit.petcare.core.model.UserType;

// DTO representing user details with full name helper method
public record UserView(
    Long id,
    String username,
    String email,
    String firstName,
    String lastName,
    UserType userType
) {
    public String fullName() {
        return firstName + " " + lastName;
    }
}