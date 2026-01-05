package gr.hua.dit.petcare.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gr.hua.dit.petcare.core.service.impl.EmailNotificationService;

@RestController
@RequestMapping("/api/test-external")
public class ExternalServiceTestController {

    private final EmailNotificationService emailService;

    public ExternalServiceTestController(EmailNotificationService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> triggerEmail(@RequestParam String email) {
        // Καλεί την εξωτερική υπηρεσία (mock)
        emailService.sendEmail(email, "Test Subject", "This is a test email from PetCare!");
        
        return ResponseEntity.ok("εξωτερική υπηρεσία κλήθηκε επιτυχώς για το email: " + email);
    }
}