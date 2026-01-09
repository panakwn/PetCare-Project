package gr.hua.dit.petcare.core.repository;

import gr.hua.dit.petcare.core.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Βρίσκει τα ραντεβού με βάση το ID του κτηνιάτρου (vet.id)
    List<Appointment> findAllByVetId(Long vetId);

    // --- ΔΙΟΡΘΩΣΗ ΕΔΩ ---
    // Αλλαγή από findAllByOwnerId σε findAllByPetOwnerId
    // Βρίσκει τα ραντεβού με βάση το ID του ιδιοκτήτη του κατοικιδίου (pet.owner.id)
    List<Appointment> findAllByPetOwnerId(Long ownerId);
    // --------------------

    boolean existsByPetIdAndDateBetween(Long petId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(a) > 0 FROM Appointment a " +
            "WHERE a.vet.id = :vetId " +
            "AND (a.startTime < :endTime AND a.endTime > :startTime)")
    boolean existsOverlappingAppointment(@Param("vetId") Long vetId,
                                         @Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime);
}