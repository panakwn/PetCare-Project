package gr.hua.dit.petcare.core.port;

// Port interface for email notification service
public interface EmailNotificationPort {
    // Sends an email to the specified recipient
    String sendEmail(String to, String subject, String body);
}