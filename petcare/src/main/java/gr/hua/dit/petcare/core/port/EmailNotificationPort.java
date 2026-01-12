package gr.hua.dit.petcare.core.port;

public interface EmailNotificationPort {
    String sendEmail(String to, String subject, String body);
}