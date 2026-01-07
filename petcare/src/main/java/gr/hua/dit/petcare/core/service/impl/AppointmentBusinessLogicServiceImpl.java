package gr.hua.dit.petcare.core.service.impl;

import gr.hua.dit.petcare.core.model.Appointment;
import gr.hua.dit.petcare.core.model.Pet;
import gr.hua.dit.petcare.core.model.User;
import gr.hua.dit.petcare.core.port.EmailNotificationPort;
import gr.hua.dit.petcare.core.repository.AppointmentRepository;
import gr.hua.dit.petcare.core.repository.PetRepository;
import gr.hua.dit.petcare.core.repository.UserRepository;
import gr.hua.dit.petcare.core.service.AppointmentBusinessLogicService;
import gr.hua.dit.petcare.core.service.model.ScheduleAppointmentRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class AppointmentBusinessLogicServiceImpl implements AppointmentBusinessLogicService {

    private final AppointmentRepository appointmentRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final EmailNotificationPort emailPort;

    public AppointmentBusinessLogicServiceImpl(AppointmentRepository appointmentRepository,
                                               PetRepository petRepository,
                                               UserRepository userRepository,
                                               EmailNotificationPort emailPort) {
        this.appointmentRepository = appointmentRepository;
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.emailPort = emailPort;
    }

    @Override
    public String scheduleAppointment(ScheduleAppointmentRequest request) {
        // 1. Βρίσκουμε το κατοικίδιο (Pet)
        Pet pet = petRepository.findById(request.petId())
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + request.petId()));

        // 2. Βρίσκουμε τον ιδιοκτήτη (User) μέσα από το κατοικίδιο
        User owner = pet.getOwner();
        if (owner == null) {
            throw new RuntimeException("Owner not found for this pet");
        }

        // 3. Βρίσκουμε τον κτηνίατρο (Vet)
        User vet = userRepository.findById(request.vetId())
                .orElseThrow(() -> new RuntimeException("Vet not found with id: " + request.vetId()));

        // --- ΕΛΕΓΧΟΣ: Ελάχιστος χρόνος 2 ημερών ανάμεσα στα ραντεβού του ίδιου ζώου ---
        LocalDateTime requestedDate = request.date();
        // Ορίζουμε το διάστημα: 2 μέρες ΠΡΙΝ και 2 μέρες ΜΕΤΑ την αιτούμενη ημερομηνία
        LocalDateTime startBound = requestedDate.minusDays(2);
        LocalDateTime endBound = requestedDate.plusDays(2);

        boolean conflictExists = appointmentRepository.existsByPetIdAndDateBetween(
                pet.getId(),
                startBound,
                endBound
        );

        if (conflictExists) {
            throw new RuntimeException("Δεν επιτρέπεται νέο ραντεβού. Πρέπει να υπάρχει κενό 2 ημερών από προηγούμενο ή επόμενο ραντεβού για το ίδιο κατοικίδιο.");
        }
        // -------------------------------------------------------------------------------

        // 4. Δημιουργία και αποθήκευση του ραντεβού
        Appointment appointment = new Appointment();
        appointment.setPet(pet);
        appointment.setVet(vet);
        appointment.setDate(request.date());
        appointment.setDescription(request.description());
        appointment.setStatus("SCHEDULED"); // Αρχική κατάσταση

        // Υπολογισμός ώρας λήξης (π.χ. 30 λεπτά διάρκεια) για μελλοντική χρήση
        appointment.setStartTime(request.date());
        appointment.setEndTime(request.date().plusMinutes(30));

        appointmentRepository.save(appointment);

        // 5. Αποστολή Email στον ιδιοκτήτη
        emailPort.sendEmail(
                owner.getEmail(),
                "Επιβεβαίωση Ραντεβού",
                "Γεια σας " + owner.getFirstName() + ", το ραντεβού για το κατοικίδιό σας (" + pet.getName() + ") καταχωρήθηκε επιτυχώς!"
        );

        return "Appointment created successfully with ID: " + appointment.getId();
    }
}