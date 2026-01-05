package gr.hua.dit.petcare.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailNotificationService {

    private final RestTemplate restTemplate;

    private final String EMAIL_PROVIDER_URL = "https://jsonplaceholder.typicode.com/posts";

    public EmailNotificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendEmail(String to, String subject, String body) {
       
        Map<String, String> request = new HashMap<>();
        request.put("to", to);
        request.put("title", subject);
        request.put("body", body);
        request.put("userId", "1"); 

        try {
           
            var response = restTemplate.postForObject(EMAIL_PROVIDER_URL, request, Object.class);
            
            System.out.println("Email notification sent via external API. Response: " + response);
        } catch (RestClientException e) {
            System.err.println("Failed to send email via external API: " + e.getMessage());
        }
    }
}