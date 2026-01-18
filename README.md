# PetCare Project

Pet care management system with Spring Boot for managing pets and appointments.

## Technologies

- Spring Boot 4.0.0
- Java 21
- PostgreSQL
- Spring Security with JWT
- Thymeleaf
- Maven

## Prerequisites

- JDK 21
- Maven
- PostgreSQL

## Configuration

Update database credentials in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://your-host:5432/your-database
    username: your-username
    password: your-password
```

## API Documentation

Swagger UI: `http://localhost:8080/swagger-ui.html`