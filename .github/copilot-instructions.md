# 🤖 Copilot Instructions – Manager User Security Project Guide

These instructions help AI coding agents understand this Spring Boot security and user management project's architecture, conventions, and developer workflows.

The goal: make the AI instantly productive in this codebase with **context-aware guidance**.

---

## 🧠 Project Overview

This project follows **clean architectural principles** with a focus on security and user management:

```
src/main/java/com/ocoelhogabriel/manager_user_security/
├── application/     ← Application services and use cases
├── config/          ← Security configurations (JWT, Web Security)
├── controller/      ← REST API endpoints
├── domain/          ← Domain entities and business logic
├── exception/       ← Global exception handling
├── handler/         ← Business logic handlers
├── infrastructure/  ← External services implementation
├── interfaces/      ← Interface definitions and API contracts
└── utils/           ← Utility classes
```

Key components:
- JWT-based authentication
- Role-based access control
- User and profile management
- Enterprise and plant management
- Logging and monitoring

---

## ⚙️ Developer Workflows

### Build & Run
```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Using Docker
docker compose up --build
```

### Testing
```bash
# Run all tests
./mvnw test

# Run with code coverage
./mvnw test jacoco:report

# Run specific test profile
./mvnw test -P performance
```

### Debugging
- Use VS Code Spring Boot extension
- Check `application.properties` for configuration
- JWT and security settings in `SecurityConfig.java`
- Database configurations via environment variables in docker-compose.yml

---

## 🧩 Conventions & Patterns

### Controller Layer
- Use `@RestController` for REST endpoints
- Implement global exception handling via `GlobalExceptionHandler`
- Follow REST naming conventions (`/api/v1/usuarios`, etc.)
- Return standard response objects for consistency

### Security Patterns
- JWT authentication filter in `JWTAuthFilter`
- Custom security configurations in `SecurityConfig`
- Role-based access control via Spring Security
- Custom exception handling for auth failures

### Domain Modeling
- Use value objects for domain primitives (Email, Username)
- Implement rich domain models with validation
- Keep business rules in the domain layer
- Use repositories for data access abstraction

### Service Layer
- Transactional operations with `@Transactional`
- Clear separation of authentication and business logic
- Logging of security events and audit trails
- Use DTOs for data transfer between layers

---

## 🌐 Integrations & Dependencies

### Core Dependencies
- Spring Boot 3.2.0
- Spring Security
- JWT Authentication (Auth0)
- JPA/Hibernate
- PostgreSQL
- Docker support
- OpenAPI/Springdoc for API documentation

### Database
- PostgreSQL for production
- H2 for testing
- Connection pooling with HikariCP
- Transaction management with Spring

### Security Integration
- JWT token management
- Custom authentication endpoints
- Role-based access control
- Cross-Origin Resource Sharing (CORS) configuration

---

## 🧭 Guidelines for AI Agents

- Always maintain security best practices
- Follow existing authentication patterns
- Use proper exception handling for security events
- Maintain audit logging where appropriate
- Keep security configurations centralized
- Follow REST API conventions established in controllers
- Use existing security annotations and filters
- Validate all user input thoroughly
- Follow clean architecture principles:
  - Domain layer should have no external dependencies
  - Application services orchestrate use cases
  - Infrastructure implements interfaces defined in domain
  - Controllers should be thin and delegate to services

---

**Last updated:** 2025-10-10  
*Maintained collaboratively between human and AI agents.*