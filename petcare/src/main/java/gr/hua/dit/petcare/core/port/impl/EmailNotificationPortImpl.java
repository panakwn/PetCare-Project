package gr.hua.dit.petcare.core.port.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import gr.hua.dit.petcare.core.port.EmailNotificationPort;

@Service
public class EmailNotificationPortImpl implements EmailNotificationPort {

    private final RestTemplate restTemplate;
    private final String emailProviderUrl;

    public EmailNotificationPortImpl(RestTemplate restTemplate,
                                     @Value("${app.external-services.email-url}") String emailProviderUrl) {
        this.restTemplate = restTemplate;
        this.emailProviderUrl = emailProviderUrl;
    }

    @Override
    public String sendEmail(String to, String subject, String body) {
        Map<String, String> request = new HashMap<>();
        request.put("to", to);
        request.put("title", subject);
        request.put("body", body);
        request.put("userId", "1"); 

        try {
            // Κλήση POST στο εξωτερικό API
            Object response = restTemplate.postForObject(emailProviderUrl, request, Object.class);
            
            System.out.println("Email notification sent via external API. Response: " + response);
            return "SUCCESS";
        } catch (RestClientException e) {
            System.err.println("Failed to send email via external API: " + e.getMessage());
            return "FAILURE";
        }
    }
}