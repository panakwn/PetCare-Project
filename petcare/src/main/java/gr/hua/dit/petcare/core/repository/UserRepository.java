package gr.hua.dit.petcare.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gr.hua.dit.petcare.core.model.User;
import gr.hua.dit.petcare.core.model.UserType;

// Repository for User entity database operations
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Finds user by username for authentication
    Optional<User> findByUsername(String username);

    // Finds user by username and eagerly loads their pets
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.pets WHERE u.username = :username")
    Optional<User> findByUsernameWithPets(@Param("username") String username);
    
    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    // Retrieves all users of a specific type (owner or veterinarian)
    List<User> findByUserType(UserType userType);
}