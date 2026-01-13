package gr.hua.dit.petcare.core.port;

public interface EmailNotificationPort {

    void sendNotification(String email, String subject, String message);
}