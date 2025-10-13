# Manager User Security - Copilot Instructions

This document provides essential knowledge for working with the Manager User Security project.

## Project Overview

This is a Java-based Spring Boot application (v3.2.3) that provides user authentication and authorization services with JWT tokens. It's packaged as a WAR file and designed to run in Tomcat 10 with JDK 17.

## Architecture & Component Structure

- **Authentication**: JWT-based authentication system implemented in `JWTAuthFilter` and `SecurityConfig`
- **API Structure**: RESTful controllers in `com.ocoelhogabriel.manager_user_security.controller` package 
- **Security Model**: URL validation and permission checking through `URLValidator` and `PermissaoHandler`
- **Spring Security**: Custom configuration with whitelist URLs in `SecurityConfig`

## Key Files & Classes

- `SecurityConfig.java`: Core security configuration with filter chain setup
- `JWTAuthFilter.java`: JWT token validation and authorization logic
- `AuthenticationController.java`: API endpoints for authentication (/api/autenticacao/v1/*)
- `Application.properties`: Contains database configs and JWT settings

## Development Workflow

### Building the Application

```shell
# Using Maven Wrapper
.\mvnw.cmd clean install

# Using Docker for local development
docker-compose up -d
```

### Configuration Requirements

- PostgreSQL database (default config: localhost:5432/silo with admin/admin credentials)
- JWT secret key (configurable via environment variable JWT_SECRET)

## API Structure

- Base path: `/manager_user_security`
- Authentication endpoints: `/api/autenticacao/v1/...`
- Swagger documentation available at: `/swagger-ui/index.html`

## Testing & Debugging

The application includes comprehensive JWT verification. When debugging authentication issues:

1. Check token validity with `/api/autenticacao/v1/validate`
2. Examine `doFilterInternal` in `JWTAuthFilter.java` for permission validation steps

## Docker Configuration

- Database: PostgreSQL 16 with custom performance tuning
- Application: Tomcat 10 with JDK 17
- Network: Both containers on the `dk-nt-silo` network
- Port mapping: Application on 8092:8080, Database on 5455:5432

## Conventions & Patterns

- Controllers use versioned paths (e.g., `/api/{resource}/v1/...`)
- Authentication responses use standardized DTOs (`ResponseAuthDTO`, `TokenValidationResponseDTO`)
- Security whitelisting for Swagger UI and authentication endpoints