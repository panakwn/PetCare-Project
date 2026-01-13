package gr.hua.dit.petcare.core.repository;

import gr.hua.dit.petcare.core.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repository for Pet entity database operations
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    // Retrieves all pets owned by a specific user
    List<Pet> findByOwnerId(Long ownerId);
}