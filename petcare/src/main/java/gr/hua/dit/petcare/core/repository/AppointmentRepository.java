package gr.hua.dit.petcare.core.repository;

import gr.hua.dit.petcare.core.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

// Repository for accessing Appointment data in the database
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Query to find appointments that overlap with a given time range for a specific vet
    @Query("SELECT a FROM Appointment a WHERE a.vet.id = :vetId AND a.status IN ('PENDING', 'CONFIRMED') AND " +
           "((a.endTime > :startTime AND a.startTime < :endTime))")
    List<Appointment> findOverlappingAppointments(@Param("vetId") Long vetId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    List<Appointment> findAllByVetId(Long vetId);
    
    @Query("SELECT a FROM Appointment a WHERE a.pet.owner.id = :ownerId")
    List<Appointment> findAllByOwnerId(@Param("ownerId") Long ownerId);
    
    List<Appointment> findByPetId(Long petId);
}