package gr.hua.dit.petcare.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

// OpenAPI configuration with JWT bearer security
@Configuration
public class OpenApiConfig {

    // Builds the OpenAPI spec and registers JWT bearer security
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("PetCare API")
                .version("v1")
                .description("API for the petcare application")
            )
            .components(new Components()
                // Scheme name must remain "bearer-key" to match controller @SecurityRequirement
                .addSecuritySchemes("bearer-key", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                )
            )
            // Apply the bearer-key requirement globally to all endpoints
            .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
    }

    // Groups REST endpoints under /api/** for documentation
    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
            .group("api")
            .packagesToScan("gr.hua.dit.petcare.web.rest")
            .pathsToMatch("/api/**")
            .build();
    }
}