# Mapeamento de Classes: Estrutura Antiga para Clean Architecture

Este documento mapeia as classes existentes da aplicação Manager User Security para a nova estrutura baseada em Clean Architecture.

## Estrutura de Pacotes da Nova Arquitetura

```
com.ocoelhogabriel.manager_user_security/
├── domain/                             # Camada de domínio (entidades e regras de negócio)
│   ├── entity/                         # Entidades de domínio 
│   ├── repository/                     # Interfaces de repositórios
│   ├── service/                        # Interfaces de serviços de domínio
│   └── valueobject/                    # Objetos de valor
│
├── application/                        # Camada de aplicação (casos de uso)
│   ├── dto/                            # DTOs de entrada e saída
│   ├── exception/                      # Exceções da camada de aplicação
│   ├── mapper/                         # Mapeadores entre entidades e DTOs
│   └── service/                        # Implementações dos serviços
│       └── impl/
│
├── infrastructure/                     # Camada de infraestrutura
│   ├── persistence/                    # Implementações de persistência
│   │   ├── entity/                     # Entidades JPA
│   │   ├── mapper/                     # Conversores entre entidades JPA e domínio
│   │   └── repository/                 # Implementações JPA dos repositórios
│   ├── security/                       # Componentes de segurança
│   │   ├── config/                     # Configurações de segurança
│   │   ├── filter/                     # Filtros de segurança
│   │   └── jwt/                        # Serviços relacionados a JWT
│   └── config/                         # Outras configurações
│
└── interfaces/                         # Camada de interfaces
    └── rest/                           # API REST
        ├── controller/                 # Controladores REST
        ├── advice/                     # Controladores de exceção
        └── assembler/                  # Montadores de recursos
```

## Mapeamento de Classes da Camada de Segurança

| Classe Antiga | Classe Nova | Responsabilidade |
|---------------|-------------|------------------|
| `com.ocoelhogabriel.manager_user_security.config.SecurityConfig` | `com.ocoelhogabriel.manager_user_security.infrastructure.security.config.WebSecurityConfig` | Configuração geral de segurança, filtros e políticas |
| `com.ocoelhogabriel.manager_user_security.config.JWTAuthFilter` | `com.ocoelhogabriel.manager_user_security.infrastructure.security.filter.JwtAuthenticationFilter` | Filtro para autenticação JWT |
| `com.ocoelhogabriel.manager_user_security.utils.JWTUtil` | `com.ocoelhogabriel.manager_user_security.infrastructure.security.jwt.JwtService` | Serviço para geração, validação e decodificação de tokens JWT |
| `com.ocoelhogabriel.manager_user_security.handler.PermissaoHandler` | `com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.PermissionEvaluator` | Avaliação de permissões de usuário |
| `com.ocoelhogabriel.manager_user_security.handler.URLValidator` | `com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.UrlPathMatcher` | Validação e classificação de URLs para autorização |
| `com.ocoelhogabriel.manager_user_security.exception.CustomAccessDeniedHandler` | `com.ocoelhogabriel.manager_user_security.infrastructure.security.handler.CustomAccessDeniedHandler` | Tratador de acesso negado |
| `com.ocoelhogabriel.manager_user_security.exception.CustomAuthenticationEntryPoint` | `com.ocoelhogabriel.manager_user_security.infrastructure.security.handler.CustomAuthenticationEntryPoint` | Ponto de entrada para autenticação |
| `com.ocoelhogabriel.manager_user_security.controller.AuthenticationController` | `com.ocoelhogabriel.manager_user_security.interfaces.rest.controller.AuthenticationController` | Controller de autenticação |
| `com.ocoelhogabriel.manager_user_security.services.AuthServInterface` | `com.ocoelhogabriel.manager_user_security.domain.service.AuthenticationService` | Interface de serviço de autenticação |
| `com.ocoelhogabriel.manager_user_security.services.impl.AuthServiceImpl` | `com.ocoelhogabriel.manager_user_security.application.service.impl.AuthenticationServiceImpl` | Implementação do serviço de autenticação |
| `com.ocoelhogabriel.manager_user_security.records.GenerateTokenRecords` | `com.ocoelhogabriel.manager_user_security.domain.valueobject.TokenDetails` | Detalhes do token gerado |
| `com.ocoelhogabriel.manager_user_security.model.dto.ResponseAuthDTO` | `com.ocoelhogabriel.manager_user_security.application.dto.response.AuthenticationResponse` | DTO de resposta de autenticação |
| `com.ocoelhogabriel.manager_user_security.model.dto.TokenValidationResponseDTO` | `com.ocoelhogabriel.manager_user_security.application.dto.response.TokenValidationResponse` | DTO de resposta de validação de token |
| `com.ocoelhogabriel.manager_user_security.model.AuthModel` | `com.ocoelhogabriel.manager_user_security.application.dto.request.AuthenticationRequest` | Modelo de autenticação |
| `com.ocoelhogabriel.manager_user_security.model.enums.RecursoMapEnum` | `com.ocoelhogabriel.manager_user_security.domain.valueobject.Resource` | Enum de recursos protegidos |

## Novas Classes a serem Criadas

| Classe Nova | Responsabilidade |
|-------------|------------------|
| `com.ocoelhogabriel.manager_user_security.infrastructure.security.config.MethodSecurityConfig` | Configuração para segurança em nível de método |
| `com.ocoelhogabriel.manager_user_security.infrastructure.security.annotation.HasPermission` | Anotação personalizada para verificação de permissão |
| `com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.CustomPermissionEvaluator` | Avaliador de permissões personalizado |
| `com.ocoelhogabriel.manager_user_security.infrastructure.security.jwt.JwtAuthenticationProvider` | Provedor de autenticação JWT |
| `com.ocoelhogabriel.manager_user_security.infrastructure.security.jwt.JwtAuthenticationToken` | Token de autenticação JWT |

## Classes de Modelo e Repositório

| Classe Antiga | Classe Nova | Responsabilidade |
|---------------|-------------|------------------|
| `com.ocoelhogabriel.manager_user_security.model.entity.Usuario` | `com.ocoelhogabriel.manager_user_security.domain.entity.User` | Entidade de domínio para usuário |
| - | `com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.UserEntity` | Entidade JPA para usuário |
| `com.ocoelhogabriel.manager_user_security.model.entity.PerfilPermissao` | `com.ocoelhogabriel.manager_user_security.domain.entity.RolePermission` | Entidade de domínio para permissão de perfil |
| - | `com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.RolePermissionEntity` | Entidade JPA para permissão de perfil |
| `com.ocoelhogabriel.manager_user_security.model.entity.Recurso` | `com.ocoelhogabriel.manager_user_security.domain.entity.Resource` | Entidade de domínio para recurso |
| - | `com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.ResourceEntity` | Entidade JPA para recurso |
| `com.ocoelhogabriel.manager_user_security.model.entity.Perfil` | `com.ocoelhogabriel.manager_user_security.domain.entity.Role` | Entidade de domínio para perfil |
| - | `com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.RoleEntity` | Entidade JPA para perfil |