package gr.hua.dit.petcare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// Configuration for REST client used to call external APIs
@Configuration
public class RestApiClientConfig {

    // Creates a RestTemplate bean for making HTTP requests
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}