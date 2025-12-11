package gr.hua.dit.petcare.core.repository;

import gr.hua.dit.petcare.core.model.User;
import gr.hua.dit.petcare.core.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository for accessing User data in the database
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<User> findByType(UserType type);
}