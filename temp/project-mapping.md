# Mapeamento entre Implementação Original e Clean Architecture

Este documento apresenta um mapeamento detalhado entre as estruturas originais do pacote `manager_user_security` e a nova implementação em `usersecurity`, identificando o status de cada componente e o que ainda precisa ser implementado.

## Estrutura do Projeto

### Antiga Estrutura (`manager_user_security`)
```
com.ocoelhogabriel.manager_user_security
  ├── OpenApiConfig.java        # Configuração do Swagger/OpenAPI
  ├── ServletInitializer.java   # Inicializador de servlet
  ├── SiloApiApplication.java   # Classe principal da aplicação
  ├── config                    # Configurações (SecurityConfig, JWTAuthFilter)
  ├── controller                # Controladores REST
  ├── domain                    # Nova estrutura de domínio (em construção)
  ├── exception                 # Exceções e handlers
  ├── handler                   # Handlers (PermissaoHandler, URLValidator)
  ├── infrastructure            # Nova estrutura de infraestrutura (em construção)
  ├── model                     # Modelos/Entidades
  │   ├── dto                   # DTOs
  │   ├── entity                # Entidades JPA
  │   └── enums                 # Enumerações
  ├── records                   # Records Java para estruturas de dados
  ├── repository                # Repositórios Spring Data JPA
  ├── services                  # Serviços de negócio 
  │   └── impl                  # Implementações de serviços
  └── utils                     # Classes utilitárias
```

### Nova Estrutura (Clean Architecture - `usersecurity`)
```
com.ocoelhogabriel.usersecurity
  ├── UserSecurityApplication.java   # Classe principal da aplicação
  │
  ├── domain                         # Camada de Domínio
  │   ├── entity                     # Entidades de domínio (puras)
  │   │   ├── Company.java           # Entidade da empresa
  │   │   ├── Logger.java            # Entidade de log do sistema
  │   │   ├── Permission.java        # Entidade de permissão
  │   │   ├── Plant.java             # Entidade de planta/unidade
  │   │   ├── Resource.java          # Entidade de recurso
  │   │   ├── Role.java              # Entidade de papel/perfil
  │   │   └── User.java              # Entidade de usuário
  │   ├── exception                  # Exceções de domínio
  │   │   ├── AuthenticationException.java
  │   │   ├── AuthorizationException.java
  │   │   ├── DomainException.java
  │   │   ├── DuplicateResourceException.java
  │   │   └── ResourceNotFoundException.java
  │   ├── repository                 # Interfaces de repositórios
  │   │   ├── CompanyRepository.java # Repositório para empresas
  │   │   ├── LoggerRepository.java  # Repositório para logs
  │   │   ├── PermissionRepository.java
  │   │   ├── PlantRepository.java   # Repositório para plantas
  │   │   ├── Repository.java
  │   │   ├── ResourceRepository.java
  │   │   ├── RoleRepository.java
  │   │   └── UserRepository.java
  │   ├── service                    # Interfaces de serviços de domínio
  │   │   ├── AuthenticationService.java
  │   │   ├── AuthorizationService.java
  │   │   ├── CompanyService.java    # Serviço de empresa
  │   │   ├── LoggerService.java     # Serviço de logs
  │   │   ├── PlantService.java      # Serviço de plantas
  │   │   └── UserService.java
  │   └── valueobject               # Objetos de valor
  │       └── LoggerType.java       # Tipo de log (enum)
  │
  ├── application                    # Camada de Aplicação
  │   ├── config                     # Configurações da aplicação
  │   ├── dto                        # DTOs internos
  │   │   └── TokenDetails.java
  │   ├── exception                  # Exceções da aplicação
  │   │   ├── ApplicationException.java
  │   │   ├── ExternalServiceException.java
  │   │   └── InvalidOperationException.java
  │   ├── service                    # Implementações de serviços
  │   │   ├── AuthenticationServiceImpl.java
  │   │   ├── AuthorizationServiceImpl.java
  │   │   ├── PermissionService.java
  │   │   ├── ResourceService.java
  │   │   ├── RoleService.java
  │   │   ├── UrlValidationService.java
  │   │   ├── UrlValidator.java
  │   │   ├── UserServiceImpl.java
  │   │   └── impl/
  │   │       ├── PermissionServiceImpl.java
  │   │       ├── ResourceServiceImpl.java
  │   │       └── RoleServiceImpl.java
  │   └── usecase                    # Casos de uso
  │       └── AuthenticationUseCase.java
  │
  ├── infrastructure                 # Camada de Infraestrutura
  │   ├── auth                       # Autenticação
  │   │   └── jwt
  │   │       └── JwtTokenProvider.java
  │   ├── config                     # Configurações gerais
  │   │   └── OpenApiConfig.java
  │   ├── persistence                # Persistência de dados
  │   │   ├── config                 # Configuração de persistência
  │   │   │   ├── AuditorAwareImpl.java
  │   │   │   └── PersistenceConfig.java
  │   │   ├── entity                 # Entidades JPA
  │   │   │   ├── CompanyEntity.java      # Entidade JPA de empresa
  │   │   │   ├── LoggerEntity.java       # Entidade JPA de log
  │   │   │   ├── PermissionEntity.java
  │   │   │   ├── PlantEntity.java        # Entidade JPA de planta
  │   │   │   ├── ResourceEntity.java
  │   │   │   ├── RoleEntity.java
  │   │   │   └── UserEntity.java
  │   │   ├── mapper                 # Mapeadores de entidades
  │   │   │   ├── CompanyMapper.java       # Mapeador de empresa
  │   │   │   ├── LoggerMapper.java        # Mapeador de log
  │   │   │   ├── PermissionMapper.java
  │   │   │   ├── PlantMapper.java         # Mapeador de planta
  │   │   │   ├── ResourceMapper.java
  │   │   │   ├── RoleMapper.java
  │   │   │   └── UserMapper.java
  │   │   └── repository             # Repositórios JPA
  │   │       ├── CompanyJpaRepository.java  # Repositório JPA de empresa
  │   │       ├── LoggerJpaRepository.java   # Repositório JPA de log
  │   │       ├── PermissionRepository.java
  │   │       ├── PlantJpaRepository.java    # Repositório JPA de planta
  │   │       ├── ResourceRepository.java
  │   │       ├── RoleRepository.java
  │   │       ├── UserRepository.java
  │   │       └── impl/
  │   │           ├── CompanyRepositoryImpl.java  # Implementação do repositório de empresa
  │   │           ├── LoggerRepositoryImpl.java   # Implementação do repositório de log
  │   │           └── PlantRepositoryImpl.java    # Implementação do repositório de planta
  │   └── security                   # Segurança e autenticação
  │       ├── SecurityConfig.java
  │       ├── config                 # Configuração de segurança
  │       │   ├── MethodSecurityConfig.java
  │       │   └── SecurityConfig.java
  │       ├── filter                 # Filtros de segurança
  │       │   └── JwtAuthenticationFilter.java
  │       ├── handler                # Handlers de segurança
  │       │   ├── CustomAccessDeniedHandler.java
  │       │   └── CustomAuthenticationEntryPoint.java
  │       ├── jwt                    # Implementação JWT
  │       │   ├── JwtAuthenticationFilter.java
  │       │   └── JwtService.java
  │       ├── permission             # Avaliação de permissões
  │       │   ├── CustomPermissionEvaluator.java
  │       │   └── CustomSecurityExpression.java
  │       ├── service                # Serviços de segurança
  │       │   └── UserDetailsServiceImpl.java
  │       └── util                   # Utilitários de segurança
  │
  └── interfaces                     # Camada de Interfaces
      ├── advice                     # Manipuladores de exceção
      │   └── GlobalExceptionHandler.java
      ├── api                        # API REST
      │   ├── auth                   # Autenticação
      │   │   └── AuthenticationController.java  # Controlador de autenticação
      │   ├── controller             # Controladores REST
      │   │   ├── PermissionController.java
      │   │   ├── ResourceController.java
      │   │   ├── RoleController.java
      │   │   └── SystemController.java
      │   ├── dto                    # DTOs da API
      │   │   ├── AuthenticationRequest.java     # DTO para requisição de autenticação
      │   │   ├── AuthenticationResponse.java    # DTO para resposta de autenticação
      │   │   ├── TokenValidationResponse.java   # DTO para validação de token
      │   │   ├── UpdatePasswordRequest.java     # DTO para atualização de senha
      │   │   ├── UpdateUserRequest.java         # DTO para atualização de usuário
      │   │   ├── UserResponse.java              # DTO para resposta de usuário
      │   │   └── UserRoleDto.java               # DTO para papel de usuário
      │   ├── mapper                 # Mapeadores (Domain -> DTO)
      │   │   ├── PermissionMapper.java
      │   │   ├── ResourceMapper.java
      │   │   └── RoleMapper.java
      │   └── resource               # Recursos da API
      │       └── UserController.java            # Controlador de usuário
      └── filter                     # Filtros de interface
```

## Mapeamento de Classes e Status de Implementação

### Modelos/Entidades

| Classe Antiga (`manager_user_security`) | Classe Nova (`usersecurity`) | Status |
|----------------------------------------|----------------------------|--------|
| `model.entity.Usuario` | `domain.entity.User` | ✅ Implementado |
| `model.entity.Perfil` | `domain.entity.Role` | ✅ Implementado |
| `model.entity.PerfilPermissao` | `domain.entity.Permission` | ✅ Implementado |
| `model.entity.Recurso` | `domain.entity.Resource` | ✅ Implementado |
| `model.EmpresaModel` | `domain.entity.Company` | ✅ Implementado |
| `model.PlantaModel` | `domain.entity.Plant` | ✅ Implementado |
| `model.LoggerModel` | `domain.entity.Logger` | ✅ Implementado |
| `model.enums.LoggerEnum` | `domain.valueobject.LoggerType` | ✅ Implementado |
| `model.AbrangenciaModel` | `domain.entity.Coverage` | ✅ Implementado |
| `model.MedicaoModel` | Removido do escopo do projeto | ⚪ Removido |
| `model.PendenciaModel` | Removido do escopo do projeto | ⚪ Removido |
| `model.SiloModel` | Removido do escopo do projeto | ⚪ Removido |
| `model.SiloModuloModel` | Removido do escopo do projeto | ⚪ Removido |
| `model.TipoSiloModel` | Removido do escopo do projeto | ⚪ Removido |

### Entidades de Persistência

| Entidade Original (`manager_user_security`) | Entidade Nova (`usersecurity`) | Status |
|--------------------------------------------|--------------------------------|--------|
| `model.entity.Usuario` | `infrastructure.persistence.entity.UserEntity` | ✅ Implementado |
| `model.entity.Perfil` | `infrastructure.persistence.entity.RoleEntity` | ✅ Implementado |
| `model.entity.PerfilPermissao` | `infrastructure.persistence.entity.PermissionEntity` | ✅ Implementado |
| `model.entity.Recurso` | `infrastructure.persistence.entity.ResourceEntity` | ✅ Implementado |
| `model.EmpresaModel` | `infrastructure.persistence.entity.CompanyEntity` | ✅ Implementado |
| `model.PlantaModel` | `infrastructure.persistence.entity.PlantEntity` | ✅ Implementado |
| `model.LoggerModel` | `infrastructure.persistence.entity.LoggerEntity` | ✅ Implementado |
| `model.AbrangenciaModel` | `infrastructure.persistence.entity.CoverageEntity` | ✅ Implementado |
| Outras entidades específicas | Implementações específicas pendentes | ❌ Pendente |

### Repositórios

| Repositório Original (`manager_user_security`) | Repositório Novo (`usersecurity`) | Status |
|------------------------------------------------|-----------------------------------|--------|
| `UsuarioRepository` | `domain.repository.UserRepository` (interface) | ✅ Implementado |
| `PerfilRepository` | `domain.repository.RoleRepository` (interface) | ✅ Implementado |
| `RecursoRepository` | `domain.repository.ResourceRepository` (interface) | ✅ Implementado |
| `PermissaoRepository` | `domain.repository.PermissionRepository` (interface) | ✅ Implementado |
| `UsuarioRepository` | `infrastructure.persistence.repository.UserRepository` (JPA) | ✅ Implementado |
| `PerfilRepository` | `infrastructure.persistence.repository.RoleRepository` (JPA) | ✅ Implementado |
| `RecursoRepository` | `infrastructure.persistence.repository.ResourceRepository` (JPA) | ✅ Implementado |
| `PermissaoRepository` | `infrastructure.persistence.repository.PermissionRepository` (JPA) | ✅ Implementado |
| `AbrangenciaRepository` | `domain.repository.CoverageRepository` (interface) | ✅ Implementado |
| `EmpresaRepository` | `domain.repository.CompanyRepository` (interface) | ✅ Implementado |
| `EmpresaRepository` | `infrastructure.persistence.repository.CompanyJpaRepository` (JPA) | ✅ Implementado |
| `EmpresaRepository` | `infrastructure.persistence.repository.impl.CompanyRepositoryImpl` (implementação) | ✅ Implementado |
| `PlantaRepository` | `domain.repository.PlantRepository` (interface) | ✅ Implementado |
| `PlantaRepository` | `infrastructure.persistence.repository.PlantJpaRepository` (JPA) | ✅ Implementado |
| `PlantaRepository` | `infrastructure.persistence.repository.impl.PlantRepositoryImpl` (implementação) | ✅ Implementado |
| `LoggerRepository` | `domain.repository.LoggerRepository` (interface) | ✅ Implementado |
| `LoggerRepository` | `infrastructure.persistence.repository.LoggerJpaRepository` (JPA) | ✅ Implementado |
| `LoggerRepository` | `infrastructure.persistence.repository.impl.LoggerRepositoryImpl` (implementação) | ✅ Implementado |
| `FirmwareRepository` | Removido do escopo do projeto | ⚪ Removido |
| `MedicaoRepository` | Removido do escopo do projeto | ⚪ Removido |
| `PendenciaRepository` | Removido do escopo do projeto | ⚪ Removido |
| `SiloModuloRepository` | Removido do escopo do projeto | ⚪ Removido |
| `SiloRepository` | Removido do escopo do projeto | ⚪ Removido |
| `TipoSiloRepository` | Removido do escopo do projeto | ⚪ Removido |

### Controladores

| Controlador Original (`manager_user_security`) | Controlador Novo (`usersecurity`) | Status |
|------------------------------------------------|-----------------------------------|--------|
| `controller.AuthenticationController` | `interfaces.api.auth.AuthenticationController` | ✅ Implementado |
| `controller.UsuarioController` | `interfaces.api.resource.UserController` | ✅ Implementado |
| `controller.PerfilController` | `interfaces.api.controller.RoleController` | ✅ Implementado |
| `controller.RecursoController` | `interfaces.api.controller.ResourceController` | ✅ Implementado |
| `controller.SecurityRestController` | `interfaces.api.controller.SystemController` | ✅ Implementado |
| `controller.PermissaoController` | `interfaces.api.controller.PermissionController` | ✅ Implementado |
| `controller.AbrangenciaController` | `interfaces.api.controller.CoverageController` | ✅ Implementado |
| `controller.EmpresaController` | Implementação específica pendente | ❌ Pendente |
| `controller.PlantaController` | Implementação específica pendente | ❌ Pendente |
| `controller.LoggerController` | Implementação específica pendente | ❌ Pendente |
| Outros controladores específicos | Implementações específicas pendentes | ❌ Pendente |

### Serviços

| Serviço Original (`manager_user_security`) | Serviço Novo (`usersecurity`) | Status |
|-------------------------------------------|------------------------------|--------|
| `services.AuthServInterface` | `domain.service.AuthenticationService` | ✅ Implementado |
| `services.impl.AuthServiceImpl` | `application.service.AuthenticationServiceImpl` | ✅ Implementado |
| Não existia | `domain.service.AuthorizationService` | ✅ Implementado |
| Não existia | `application.service.AuthorizationServiceImpl` | ✅ Implementado |
| Não existia explicitamente | `domain.service.UserService` | ✅ Implementado |
| Não existia explicitamente | `application.service.UserServiceImpl` | ✅ Implementado |
| Não existia explicitamente | `application.service.PermissionService` | ✅ Implementado |
| Não existia explicitamente | `application.service.impl.PermissionServiceImpl` | ✅ Implementado |
| Não existia explicitamente | `application.service.ResourceService` | ✅ Implementado |
| Não existia explicitamente | `application.service.impl.ResourceServiceImpl` | ✅ Implementado |
| Não existia explicitamente | `application.service.RoleService` | ✅ Implementado |
| Não existia explicitamente | `application.service.impl.RoleServiceImpl` | ✅ Implementado |
| `handler.URLValidator` | `application.service.UrlValidator` | ✅ Implementado |
| Não existia | `application.service.UrlValidationService` | ✅ Implementado |
| Não existia explicitamente | `domain.service.CompanyService` | ✅ Implementado |
| Não existia explicitamente | `domain.service.PlantService` | ✅ Implementado |
| Não existia explicitamente | `domain.service.LoggerService` | ✅ Implementado |
| Não existia explicitamente | `domain.service.CoverageService` | ✅ Implementado |
| Não existia explicitamente | `application.service.impl.CoverageServiceImpl` | ✅ Implementado |
| Outros serviços específicos | Implementações específicas pendentes | ❌ Pendente |

### Segurança e Autenticação

| Componente Original (`manager_user_security`) | Componente Novo (`usersecurity`) | Status |
|----------------------------------------------|----------------------------------|--------|
| `config.SecurityConfig` | `infrastructure.security.config.SecurityConfig` | ✅ Implementado |
| `config.JWTAuthFilter` | `infrastructure.security.filter.JwtAuthenticationFilter` | ✅ Implementado |
| `utils.JWTUtil` | `infrastructure.security.jwt.JwtService` | ✅ Implementado |
| `handler.PermissaoHandler` | `infrastructure.security.permission.CustomPermissionEvaluator` | ✅ Implementado |
| `exception.CustomAccessDeniedHandler` | `infrastructure.security.handler.CustomAccessDeniedHandler` | ✅ Implementado |
| `exception.CustomAuthenticationEntryPoint` | `infrastructure.security.handler.CustomAuthenticationEntryPoint` | ✅ Implementado |
| Não existia | `infrastructure.security.config.MethodSecurityConfig` | ✅ Implementado |
| Não existia | `infrastructure.security.permission.CustomSecurityExpression` | ✅ Implementado |
| Não existia | `infrastructure.security.service.UserDetailsServiceImpl` | ✅ Implementado |
| Não existia | `infrastructure.auth.jwt.JwtTokenProvider` | ✅ Implementado |

### DTOs e Objetos de Valor

| Original (`manager_user_security`) | Novo (`usersecurity`) | Status |
|------------------------------------|----------------------|--------|
| `records.GenerateTokenRecords` | `application.dto.TokenDetails` | ✅ Implementado |
| `model.AuthModel` | `interfaces.api.dto.AuthenticationRequest` | ✅ Implementado |
| `model.dto.ResponseAuthDTO` | `interfaces.api.dto.AuthenticationResponse` | ✅ Implementado |
| `model.dto.TokenValidationResponseDTO` | `interfaces.api.dto.TokenValidationResponse` | ✅ Implementado |
| `model.dto.UserDTO` | `interfaces.api.dto.UserResponse` | ✅ Implementado |
| `model.dto.UserRoleDTO` | `interfaces.api.dto.UserRoleDto` | ✅ Implementado |
| `model.dto.UserUpdateDTO` | `interfaces.api.dto.UpdateUserRequest` | ✅ Implementado |
| `model.dto.PasswordUpdateDTO` | `interfaces.api.dto.UpdatePasswordRequest` | ✅ Implementado |
| `model.enums.LoggerEnum` | `domain.valueobject.LoggerType` | ✅ Implementado |
| Outros DTOs específicos | Implementações específicas pendentes | ❌ Pendente |

## Exceções

| Exceção Original (`manager_user_security`) | Exceção Nova (`usersecurity`) | Status |
|-------------------------------------------|-------------------------------|--------|
| `exception.CustomMessageExcep` | `application.exception.ApplicationException` | ✅ Implementado |
| Não existia | `domain.exception.AuthenticationException` | ✅ Implementado |
| Não existia | `domain.exception.AuthorizationException` | ✅ Implementado |
| Não existia | `domain.exception.DomainException` | ✅ Implementado |
| Não existia | `domain.exception.DuplicateResourceException` | ✅ Implementado |
| Não existia | `domain.exception.ResourceNotFoundException` | ✅ Implementado |
| Não existia | `application.exception.ExternalServiceException` | ✅ Implementado |
| Não existia | `application.exception.InvalidOperationException` | ✅ Implementado |

### Mapeadores

| Original (`manager_user_security`) | Novo (`usersecurity`) | Status |
|------------------------------------|----------------------|--------|
| Não existia explicitamente | `infrastructure.persistence.mapper.UserMapper` | ✅ Implementado |
| Não existia explicitamente | `infrastructure.persistence.mapper.RoleMapper` | ✅ Implementado |
| Não existia explicitamente | `infrastructure.persistence.mapper.ResourceMapper` | ✅ Implementado |
| Não existia explicitamente | `infrastructure.persistence.mapper.PermissionMapper` | ✅ Implementado |
| Não existia explicitamente | `infrastructure.persistence.mapper.CompanyMapper` | ✅ Implementado |
| Não existia explicitamente | `infrastructure.persistence.mapper.PlantMapper` | ✅ Implementado |
| Não existia explicitamente | `infrastructure.persistence.mapper.LoggerMapper` | ✅ Implementado |
| Não existia explicitamente | `infrastructure.persistence.mapper.CoverageMapper` | ✅ Implementado |
| Não existia explicitamente | `interfaces.api.mapper.PermissionMapper` | ✅ Implementado |
| Não existia explicitamente | `interfaces.api.mapper.ResourceMapper` | ✅ Implementado |
| Não existia explicitamente | `interfaces.api.mapper.RoleMapper` | ✅ Implementado |
| Não existia explicitamente | `interfaces.api.mapper.CompanyMapper` | ✅ Implementado |
| Não existia explicitamente | `interfaces.api.mapper.PlantMapper` | ✅ Implementado |
| Não existia explicitamente | `interfaces.api.mapper.LoggerMapper` | ✅ Implementado |
| Não existia explicitamente | `interfaces.api.mapper.CoverageMapper` | ✅ Implementado |
| Outros mapeadores específicos | Implementações específicas pendentes | ❌ Pendente |

### Casos de Uso

| Original (`manager_user_security`) | Novo (`usersecurity`) | Status |
|------------------------------------|----------------------|--------|
| Não existia como componente separado | `application.usecase.AuthenticationUseCase` | ✅ Implementado |
| Não existia como componente separado | `application.usecase.CompanyUseCase` | ✅ Implementado |
| Não existia como componente separado | `application.usecase.PlantUseCase` | ✅ Implementado |
| Não existia como componente separado | `application.usecase.LoggerUseCase` | ✅ Implementado |
| Não existia como componente separado | `application.usecase.CoverageUseCase` | ✅ Implementado |
| Outros casos de uso específicos | Implementações específicas pendentes | ❌ Pendente |

## Principais Componentes Implementados

- [x] Security Config (JwtAuthenticationFilter, SecurityConfig)
- [x] Autenticação (JwtService, AuthenticationService)
- [x] Avaliação de Permissões (PermissionEvaluator)
- [x] Controladores principais de segurança
- [x] DTOs para autenticação e segurança
- [x] Entidades de domínio para User, Role, Permission, Resource
- [x] Entidades de domínio para Company, Plant, Logger
- [x] Repositórios para as principais entidades
- [x] Tratamento de exceções

## Componentes Completos e Remoções de Escopo

A seguir está um resumo dos componentes implementados e aqueles removidos do escopo:

1. **Controladores da API REST Implementados**: 
   - [x] AuthenticationController
   - [x] UserController
   - [x] RoleController (implementado e aprimorado com RoleUseCase)
   - [x] ResourceController
   - [x] PermissionController
   - [x] CompanyController
   - [x] PlantController
   - [x] LoggerController
   - [x] CoverageController

2. **DTOs da API Implementados**:
   - [x] AuthenticationRequest/Response
   - [x] TokenValidationResponse
   - [x] UserResponse e UserRequest
   - [x] UpdatePasswordRequest
   - [x] CompanyRequest/Response
   - [x] PlantRequest/Response
   - [x] LoggerRequest/Response
   - [x] CoverageRequest/Response

3. **Casos de Uso Implementados**:
   - [x] AuthenticationUseCase
   - [x] RoleUseCase
   - [x] CompanyUseCase
   - [x] PlantUseCase
   - [x] LoggerUseCase
   - [x] CoverageUseCase

4. **Implementações de Serviços Completos**:
   - [x] CompanyServiceImpl
   - [x] PlantServiceImpl
   - [x] LoggerServiceImpl
   - [x] CoverageServiceImpl
   - [x] UserServiceImpl
   - [x] RoleServiceImpl
   - [x] ResourceServiceImpl
   - [x] PermissionServiceImpl
   - [x] AuthenticationServiceImpl
   - [x] AuthorizationServiceImpl

5. **Entidades de Domínio Implementadas**:
   - [x] User
   - [x] Role
   - [x] Permission
   - [x] Resource
   - [x] Company
   - [x] Plant
   - [x] Logger
   - [x] Coverage (Abrangência)

### Componentes Removidos do Escopo

Os seguintes componentes foram removidos do escopo do projeto após análise e decisão da equipe:

1. **Entidades Removidas**:
   - ⚪ Silo
   - ⚪ SiloModulo/SiloModule
   - ⚪ TipoSilo/SiloType
   - ⚪ Medicao/Measurement
   - ⚪ Pendencia/Pendency
   - ⚪ Firmware

2. **Controladores Removidos**:
   - ⚪ SiloController
   - ⚪ SiloModuleController
   - ⚪ SiloTypeController
   - ⚪ MeasurementController
   - ⚪ PendencyController
   - ⚪ FirmwareController

3. **Casos de Uso Removidos**:
   - ⚪ SiloUseCase
   - ⚪ SiloModuleUseCase
   - ⚪ SiloTypeUseCase
   - ⚪ MeasurementUseCase
   - ⚪ PendencyUseCase
   - ⚪ FirmwareUseCase

6. **Testes**:
   - [ ] Testes unitários
   - [ ] Testes de integração
   - [ ] Testes end-to-end

7. **Migração de Dados**:
   - [ ] Estratégia para migração de dados entre estruturas antigas e novas

## Progresso Atual

A migração para Clean Architecture está em andamento, com avanços significativos realizados:

1. ✅ **Implementação da estrutura de domínio**: Entidades principais implementadas seguindo padrões de design como Builder e Object Value.
2. ✅ **Implementação da camada de persistência**: Repositórios JPA e mapeadores implementados para as principais entidades.
3. ✅ **Implementação da autenticação e segurança**: Sistema JWT completo com filtros, validadores e serviços relacionados.
4. ✅ **Implementação de APIs REST principais**: Controladores para autenticação e gerenciamento de usuários e permissões.
5. ✅ **Implementação de domínios específicos**: Company, Plant, Logger e Coverage (Abrangência) implementados seguindo os princípios de Clean Architecture.

## Próximos Passos Recomendados

1. **Implementar testes**:
   - [ ] Desenvolver testes unitários para entidades de domínio
   - [ ] Desenvolver testes para casos de uso e serviços
   - [ ] Desenvolver testes de integração para repositórios
   - [ ] Implementar testes end-to-end para controladores REST
   - [ ] Configurar relatórios de cobertura de código

2. **Melhorar documentação**:
   - [ ] Aprimorar documentação OpenAPI/Swagger com exemplos de requisição/resposta
   - [ ] Documentar cenários de erro e códigos de status
   - [ ] Criar guias de uso para APIs principais
   - [ ] Documentar processos de autenticação e autorização

3. **Implementar estratégia de migração de dados**:
   - [ ] Mapear estruturas de dados antigas para novas
   - [ ] Desenvolver scripts de migração para dados existentes
   - [ ] Criar procedimentos de backup e rollback
   - [ ] Planejar janela de migração com mínimo impacto

4. **Melhorias de qualidade**:
   - [ ] Configurar análise estática de código
   - [ ] Implementar logging estruturado
   - [ ] Configurar monitoramento de performance
   - [ ] Implementar métricas de uso e desempenho

5. **Tarefas de finalização**:
   - [ ] Remover código antigo não utilizado
   - [ ] Realizar testes finais de integração
   - [ ] Preparar ambiente de produção
   - [ ] Elaborar plano de implantação e rollback
