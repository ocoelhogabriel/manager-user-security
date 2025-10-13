# Contribution Guide

This document establishes the conventions and practices to be followed when contributing to the Manager User Security project.

## General Principles

1. **Clean Code**: Write clean, readable and well-documented code
2. **Test First**: Adopt TDD practices when possible
3. **Clean Architecture**: Maintain separation of responsibilities between layers
4. **Security First**: Think about security in every line of code

## Coding Standards

### Naming Conventions

- **Classes**: CamelCase (e.g., `UserService`)
- **Interfaces**: CamelCase, optional "I" prefix (e.g., `UserRepository` or `IUserRepository`)
- **Methods**: camelCase (e.g., `findByUsername()`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_LOGIN_ATTEMPTS`)
- **Packages**: lowercase, no underscore (e.g., `com.example.domain`)
- **Variables**: camelCase, descriptive names (e.g., `userRepository` not `repo`)

### Code Organization

- One file per class/interface/enum
- Limit of 1000 lines per file
- Maximum of 200 lines per method
- Maximum of 7 parameters per method
- Maximum of 5 indentation levels

### Comments and Documentation

- Use Javadoc for public classes and methods
- Comments in English and objective
- Explain "why", not "what" the code does
- Document architectural decisions and trade-offs

### Error Handling

- Specific exceptions instead of generic ones
- Detailed exception logging (with context)
- Never suppress exceptions without proper handling
- Use clear error messages for the end user

## Development Workflow

### Branches

- `main`: Production code
- `develop`: Next version in development
- `feature/xxx`: For new features
- `bugfix/xxx`: For bug fixes
- `hotfix/xxx`: For urgent fixes in production

### Commits

- Messages in English
- Format: `<type>: <description>` (e.g., `feat: add user authentication`)
- Common types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`
- Concise and focused descriptions (max 50 characters)

### Pull Requests

- Clear description of what has been changed
- Link to issues when applicable
- Include tests for new features
- Request review from at least 1 person
- Ensure automatic tests pass

## Tests

### Test Types

- **Unit Tests**: For individual classes
- **Integration Tests**: For complete flows
- **API Tests**: For REST endpoints
- **Performance Tests**: For critical cases

### Minimum Coverage

- General: 80%
- Domain: 90%
- Services: 85%
- Infrastructure: 70%

### Test Conventions

- One assertion per test when possible
- Descriptive names (`shouldReturnUserWhenValidIdIsProvided`)
- AAA pattern (Arrange-Act-Assert) or Given-When-Then
- Mock only external dependencies

## Security

### General Rules

- Never store passwords in plain text
- Always use HTTPS in production
- Implement protection against CSRF
- Validate all user inputs
- Limit login attempts
- Rotate secrets and tokens regularly

### Implementations

- Use BCrypt for password hashing
- JWT for authentication tokens
- Spring Security for access control
- Input sanitization to prevent XSS and SQL injection

## Maintenance and Evolution

### Refactoring

- Identify and fix code smells
- Eliminate circular dependencies
- Reduce coupling between components
- Increase cohesion of classes and methods

### Monitoring

- Configure structured logs
- Implement tracking of critical events
- Monitor performance and response times
- Alerts for security failures

## Review and Approval

This guide should be reviewed and updated quarterly to ensure it is aligned with best practices and project needs.

---

Last updated: 10/12/2025