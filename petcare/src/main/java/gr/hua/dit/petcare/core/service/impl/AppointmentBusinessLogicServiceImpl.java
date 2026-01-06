package gr.hua.dit.petcare.core.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gr.hua.dit.petcare.core.model.Appointment;
import gr.hua.dit.petcare.core.model.Pet;
import gr.hua.dit.petcare.core.model.User;
import gr.hua.dit.petcare.core.port.EmailNotificationPort;
import gr.hua.dit.petcare.core.repository.AppointmentRepository;
import gr.hua.dit.petcare.core.repository.PetRepository;
import gr.hua.dit.petcare.core.repository.UserRepository;
import gr.hua.dit.petcare.core.service.AppointmentBusinessLogicService;
import gr.hua.dit.petcare.core.service.model.ScheduleAppointmentRequest;

@Service
@Transactional
public class AppointmentBusinessLogicServiceImpl implements AppointmentBusinessLogicService {

    private final AppointmentRepository appointmentRepository;
    private final PetRepository petRepository;   // Χρειαζόμαστε το PetRepository
    private final UserRepository userRepository; // Χρειαζόμαστε το UserRepository για τον Κτηνίατρο
    private final EmailNotificationPort emailPort;

    // Constructor Injection
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
        // Στα records δεν χρησιμοποιούμε getPetId(), αλλά petId()
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

        // --- ΕΔΩ ΘΑ ΜΠΟΡΟΥΣΕΣ ΝΑ ΒΑΛΕΙΣ ΕΛΕΓΧΟΥΣ ΓΙΑ ΔΙΠΛΑ ΡΑΝΤΕΒΟΥ ---
        // π.χ. if (appointmentRepository.existsByVetAndDate(...)) { ... }

        // 4. Δημιουργία και αποθήκευση του ραντεβού
        Appointment appointment = new Appointment();
        appointment.setPet(pet);  // Συνδέουμε το Pet
        appointment.setVet(vet);  // Συνδέουμε τον Vet
        appointment.setDate(request.date()); // Ημερομηνία από το record
        appointment.setDescription(request.description()); // Περιγραφή
        appointment.setStatus("SCHEDULED"); // Αρχική κατάσταση

        appointmentRepository.save(appointment);

        // 5. Αποστολή Email στον ιδιοκτήτη
        // Διόρθωση: getFirstName() αντί για getFirstname()
        emailPort.sendEmail(
                owner.getEmail(),
                "Επιβεβαίωση Ραντεβού",
                "Γεια σας " + owner.getFirstName() + ", το ραντεβού για το κατοικίδιό σας (" + pet.getName() + ") καταχωρήθηκε επιτυχώς!"
        );

        return "Appointment created successfully with ID: " + appointment.getId();
    }
}