package gr.hua.dit.petcare.core.port;

public interface EmailNotificationPort {
    /**
     * Sends an email notification.
     *
     * @param to      The recipient email address.
     * @param subject The subject of the email.
     * @param body    The body content of the email.
     * @return        A result string or status (optional, can be void).
     */
    String sendEmail(String to, String subject, String body);
}