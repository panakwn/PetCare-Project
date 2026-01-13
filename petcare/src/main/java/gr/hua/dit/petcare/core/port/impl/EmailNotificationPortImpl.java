package gr.hua.dit.petcare.core.port.impl;

import gr.hua.dit.petcare.core.port.EmailNotificationPort;
import gr.hua.dit.petcare.core.port.impl.dto.SendEmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class EmailNotificationPortImpl implements EmailNotificationPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationPortImpl.class);
    private final RestTemplate restTemplate;

    @Value("${external.email-service.url}")
    private String emailServiceUrl;

    public EmailNotificationPortImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void sendNotification(String email, String subject, String message) {
        LOGGER.info("Attempting to send email via External REST API to: {}", email);

        SendEmailRequest request = new SendEmailRequest(subject, message, email);


        try {
            Object response = restTemplate.postForObject(emailServiceUrl, request, Object.class);


            LOGGER.info("Email sent successfully! External Service Response: {}", response);
        } catch (Exception e) {
            LOGGER.error("Failed to send email via external service", e);
            }
    }
}