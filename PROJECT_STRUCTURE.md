# Mapeamento da Estrutura do Projeto

Este documento descreve a estrutura atual de pacotes e classes do projeto `manager-user-security`, com o objetivo de fornecer uma visão clara e identificar inconsistências, como arquivos duplicados.

## Estrutura de Pacotes e Classes

A seguir, a listagem completa da estrutura de `src/main/java/com/ocoelhogabriel/manager_user_security`.

### 1. Camada de Domínio (`domain`)

-   **`domain/entity`**
    -   `Company.java`
    -   `Coverage.java`
    -   `Logger.java`
    -   `Permission.java`
    -   `Plant.java`
    -   `Resource.java`
    -   `Role.java`
    -   `User.java`
-   **`domain/exception`**
    -   `AuthenticationException.java`
    -   `AuthorizationException.java`
    -   `DomainException.java`
    -   `DuplicateResourceException.java`
    -   `ResourceNotFoundException.java`
-   **`domain/repository`**
    -   `CompanyRepository.java`
    -   `CoverageRepository.java`
    -   `LoggerRepository.java`
    -   `PermissionRepository.java`
    -   `PlantRepository.java`
    -   `Repository.java`
    -   `ResourceRepository.java`
    -   `RoleRepository.java`
    -   `UserRepository.java`
-   **`domain/service`**
    -   `AuthenticationService.java`
    -   `AuthorizationService.java`
    -   `CompanyService.java`
    -   `CoverageService.java`
    -   `LoggerService.java`
    -   `PermissionService.java`
    -   `PlantService.java`
    -   `ResourceService.java`
    -   `RolePermissionService.java`
    -   `RoleService.java`
    -   `UserService.java`
-   **`domain/valueobject`**
    -   `HttpMethod.java`
    -   `LoggerType.java`

### 2. Camada de Interfaces (`interfaces`)

-   **`interfaces/advice`**
    -   `GlobalExceptionHandler.java`
-   **`interfaces/controller` (!!! PACOTE OBSOLETO !!!)**
    -   `CompanyController.java` **(!!! DUPLICADO !!!)**
    -   `CoverageController.java` **(!!! DUPLICADO !!!)**
    -   **`auth`**
        -   `AuthenticationController.java` **(!!! DUPLICADO !!!)**
    -   **`resource`** (Vazio)
-   **`interfaces/controllers` (Pacote Correto)**
    -   `CompanyController.java`
    -   `CoverageController.java`
    -   `UserController.java`
    -   **`auth`**
        -   `AuthenticationController.java`
-   **`interfaces/dto`**
    -   `AuthenticationRequest.java`
    -   `AuthenticationResponse.java`
    -   `CompanyRequest.java`
    -   `CompanyResponse.java`
    -   `CompanyUpdateRequest.java`
    -   `CoverageRequest.java`
    -   `CoverageResponse.java`
    -   `CoverageUpdateRequest.java`
    -   `CreateUserRequest.java`
    -   `ErrorResponse.java`
    -   `LoggerRequest.java`
    -   `LoggerResponse.java`
    -   `PlantRequest.java`
    -   `PlantResponse.java`
    -   `PlantUpdateRequest.java`
    -   `TokenValidationResponse.java`
    -   `UpdatePasswordRequest.java`
    -   `UpdateUserRequest.java`
    -   `UserResponse.java`
    -   `UserRoleDto.java`
    -   **`permission`**
        -   `CreatePermissionRequest.java`
        -   `PermissionResponse.java`
        -   `UpdatePermissionRequest.java`
    -   **`resource`**
        -   `CreateResourceRequest.java`
        -   `ResourceResponse.java`
        -   `UpdateResourceRequest.java`
    -   **`role`**
        -   `AddPermissionRequest.java`
        -   `CreateRoleRequest.java`
        -   `RoleResponse.java`
        -   `UpdateRoleRequest.java`
-   **`interfaces/mapper`**
    -   `CompanyMapper.java`
    -   `CoverageMapper.java`
    -   `LoggerMapper.java`
    -   `PermissionMapper.java`
    -   `PlantMapper.java`
    -   `ResourceMapper.java`
    -   `RoleMapper.java`

### 3. Camada de Aplicação (`application`)

-   **`application/dto`**
    -   `TokenDetails.java`
-   **`application/exception`**
    -   `ApplicationException.java`
    -   `ExternalServiceException.java`
    -   `InvalidOperationException.java`
-   **`application/service`**
    -   `AuthenticationServiceImpl.java`
    -   `AuthorizationServiceImpl.java`
    -   `CompanyServiceImpl.java`
    -   `CoverageServiceImpl.java`
    -   `LoggerServiceImpl.java`
    -   `PermissionServiceImpl.java`
    -   `PlantServiceImpl.java`
    -   `ResourceServiceImpl.java`
    -   `RolePermissionServiceImpl.java`
    -   `RoleServiceImpl.java`
    -   `UrlValidationService.java`
    -   `UserServiceImpl.java`
-   **`application/validator`**
    -   `UrlValidator.java`

### 4. Camada de Infraestrutura (`infrastructure`)

-   **`infrastructure/config`**
    -   `OpenApiConfig.java`
-   **`infrastructure/persistence`**
    -   **`adapter`**
        -   `CompanyRepositoryAdapter.java`
        -   `CoverageRepositoryAdapter.java`
        -   `LoggerRepositoryAdapter.java`
        -   `PermissionRepositoryAdapter.java`
        -   `PlantRepositoryAdapter.java`
        -   `ResourceRepositoryAdapter.java`
        -   `RoleRepositoryAdapter.java`
        -   `UserRepositoryAdapter.java`
    -   **`entity`**
        -   `CompanyEntity.java`
        -   `CoverageEntity.java`
        -   `LoggerEntity.java`
        -   `PermissionEntity.java`
        -   `PlantEntity.java`
        -   `ResourceEntity.java`
        -   `RoleEntity.java`
        -   `UserEntity.java`
    -   **`mapper`**
        -   `CompanyMapper.java`
        -   `CoverageMapper.java`
        -   `LoggerMapper.java`
        -   `PermissionMapper.java`
        -   `PlantMapper.java`
        -   `ResourceMapper.java`
        -   `RoleMapper.java`
        -   `UserMapper.java`
    -   **`repository`**
        -   `CompanyJpaRepository.java`
        -   `CoverageJpaRepository.java`
        -   `LoggerJpaRepository.java`
        -   `PermissionJpaRepository.java`
        -   `PlantJpaRepository.java`
        -   `ResourceJpaRepository.java`
        -   `RoleJpaRepository.java`
        -   `UserJpaRepository.java`
-   **`infrastructure/security`**
    -   `SecurityConfig.java`
    -   **`jwt`**
        -   `JwtAuthenticationFilter.java`
        -   `JwtManager.java`
    -   **`permission`**
        -   `CustomPermissionEvaluator.java`
        -   `CustomSecurityExpression.java`
    -   **`service`**
        -   `ResourceServiceAdapter.java`
        -   `UserDetailsServiceImpl.java`
    -   **`util`**
        -   `SecurityConstants.java`

---

## Resumo das Inconsistências

-   **Pacote Obsoleto**: O pacote `interfaces/controller` é redundante e deve ser removido.
-   **Classes Duplicadas**: As seguintes classes existem tanto no pacote obsoleto (`controller`) quanto no pacote correto (`controllers`):
    -   `CompanyController.java`
    -   `CoverageController.java`
    -   `auth/AuthenticationController.java`

## Próximos Passos Recomendados

1.  **Remover o pacote obsoleto**: Excluir todo o diretório `src/main/java/com/ocoelhogabriel/manager_user_security/interfaces/controller`.
2.  **Validar o Projeto**: Após a remoção, compilar e executar os testes para garantir que a aplicação continua funcionando como esperado e que nenhuma referência ao pacote antigo permaneceu.
