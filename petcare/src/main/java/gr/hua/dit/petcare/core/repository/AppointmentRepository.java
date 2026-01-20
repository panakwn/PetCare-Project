package gr.hua.dit.petcare.core.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gr.hua.dit.petcare.core.model.Appointment;
import gr.hua.dit.petcare.core.model.User;

// Repository for Appointment entity database operations
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Retrieves active and scheduled appointments for a veterinarian
    @Query("SELECT a FROM Appointment a WHERE a.vet.id = :vetId AND (a.endTime > :now OR a.status = 'SCHEDULED') ORDER BY a.startTime ASC")
    List<Appointment> findActiveByVetId(@Param("vetId") Long vetId, @Param("now") LocalDateTime now);

    // Retrieves active and scheduled appointments for a pet owner
    @Query("SELECT a FROM Appointment a WHERE a.pet.owner.id = :ownerId AND (a.endTime > :now OR a.status = 'SCHEDULED') ORDER BY a.startTime ASC")
    List<Appointment> findActiveByPetOwnerId(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now);

    List<Appointment> findAllByVetId(Long vetId);
    List<Appointment> findAllByPetOwnerId(Long ownerId);

    List<Appointment> findAllByPetIdOrderByDateDesc(Long petId);

    boolean existsByPetIdAndDateBetween(Long petId, LocalDateTime start, LocalDateTime end);

    // Checks if a veterinarian has overlapping appointments in the given time period
    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.vet.id = :vetId AND a.startTime IS NOT NULL AND a.endTime IS NOT NULL AND (a.startTime < :endTime AND a.endTime > :startTime)")
    boolean existsOverlappingAppointment(@Param("vetId") Long vetId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    List<Appointment> findByVet(User vet);

    List<Appointment> findByPet_Owner(User owner);
}