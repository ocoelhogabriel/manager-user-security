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
  │   │   ├── Permission.java
  │   │   ├── Resource.java
  │   │   ├── Role.java
  │   │   └── User.java
  │   ├── exception                  # Exceções de domínio
  │   │   ├── AuthenticationException.java
  │   │   ├── AuthorizationException.java
  │   │   ├── DomainException.java
  │   │   ├── DuplicateResourceException.java
  │   │   └── ResourceNotFoundException.java
  │   ├── repository                 # Interfaces de repositórios
  │   │   ├── PermissionRepository.java
  │   │   ├── Repository.java
  │   │   ├── ResourceRepository.java
  │   │   ├── RoleRepository.java
  │   │   └── UserRepository.java
  │   └── service                    # Interfaces de serviços de domínio
  │       ├── AuthenticationService.java
  │       ├── AuthorizationService.java
  │       └── UserService.java
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
  │   │   │   ├── PermissionEntity.java
  │   │   │   ├── ResourceEntity.java
  │   │   │   ├── RoleEntity.java
  │   │   │   └── UserEntity.java
  │   │   ├── mapper                 # Mapeadores de entidades
  │   │   │   ├── PermissionMapper.java
  │   │   │   ├── ResourceMapper.java
  │   │   │   ├── RoleMapper.java
  │   │   │   └── UserMapper.java
  │   │   └── repository             # Repositórios JPA
  │   │       ├── PermissionRepository.java
  │   │       ├── ResourceRepository.java
  │   │       ├── RoleRepository.java
  │   │       ├── UserRepository.java
  │   │       └── impl/
  │   └── security                   # Segurança e autenticação
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
      │   ├── controller             # Controladores REST
      │   ├── dto                    # DTOs da API
      │   ├── mapper                 # Mapeadores (Domain -> DTO)
      │   └── resource               # Recursos da API
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
| `model.AbrangenciaModel` | Implementação específica pendente | ❌ Pendente |
| `model.EmpresaModel` | Implementação específica pendente | ❌ Pendente |
| `model.LoggerModel` | Implementação específica pendente | ❌ Pendente |
| `model.MedicaoModel` | Implementação específica pendente | ❌ Pendente |
| `model.PendenciaModel` | Implementação específica pendente | ❌ Pendente |
| `model.PlantaModel` | Implementação específica pendente | ❌ Pendente |
| `model.SiloModel` | Implementação específica pendente | ❌ Pendente |
| `model.SiloModuloModel` | Implementação específica pendente | ❌ Pendente |
| `model.TipoSiloModel` | Implementação específica pendente | ❌ Pendente |

### Entidades de Persistência

| Entidade Original (`manager_user_security`) | Entidade Nova (`usersecurity`) | Status |
|--------------------------------------------|--------------------------------|--------|
| `model.entity.Usuario` | `infrastructure.persistence.entity.UserEntity` | ✅ Implementado |
| `model.entity.Perfil` | `infrastructure.persistence.entity.RoleEntity` | ✅ Implementado |
| `model.entity.PerfilPermissao` | `infrastructure.persistence.entity.PermissionEntity` | ✅ Implementado |
| `model.entity.Recurso` | `infrastructure.persistence.entity.ResourceEntity` | ✅ Implementado |
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
| `AbrangenciaRepository` | Implementação específica pendente | ❌ Pendente |
| `EmpresaRepository` | Implementação específica pendente | ❌ Pendente |
| `FirmwareRepository` | Implementação específica pendente | ❌ Pendente |
| `LoggerRepository` | Implementação específica pendente | ❌ Pendente |
| `MedicaoRepository` | Implementação específica pendente | ❌ Pendente |
| `PendenciaRepository` | Implementação específica pendente | ❌ Pendente |
| `PlantaRepository` | Implementação específica pendente | ❌ Pendente |
| `SiloModuloRepository` | Implementação específica pendente | ❌ Pendente |
| `SiloRepository` | Implementação específica pendente | ❌ Pendente |
| `TipoSiloRepository` | Implementação específica pendente | ❌ Pendente |

### Controladores

| Controlador Original (`manager_user_security`) | Controlador Novo (`usersecurity`) | Status |
|------------------------------------------------|-----------------------------------|--------|
| `controller.AuthenticationController` | Implementação específica pendente | ❌ Pendente |
| `controller.UsuarioController` | Implementação específica pendente | ❌ Pendente |
| `controller.PerfilController` | Implementação específica pendente | ❌ Pendente |
| `controller.RecursoController` | Implementação específica pendente | ❌ Pendente |
| `controller.SecurityRestController` | Implementação específica pendente | ❌ Pendente |
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
| `model.AuthModel` | Implementação específica pendente | ❌ Pendente |
| `model.dto.ResponseAuthDTO` | Implementação específica pendente | ❌ Pendente |
| `model.dto.TokenValidationResponseDTO` | Implementação específica pendente | ❌ Pendente |
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

## Mapeadores

| Original (`manager_user_security`) | Novo (`usersecurity`) | Status |
|------------------------------------|----------------------|--------|
| Não existia explicitamente | `infrastructure.persistence.mapper.UserMapper` | ✅ Implementado |
| Não existia explicitamente | `infrastructure.persistence.mapper.RoleMapper` | ✅ Implementado |
| Não existia explicitamente | `infrastructure.persistence.mapper.ResourceMapper` | ✅ Implementado |
| Não existia explicitamente | `infrastructure.persistence.mapper.PermissionMapper` | ✅ Implementado |
| Outros mapeadores específicos | Implementações específicas pendentes | ❌ Pendente |

## Casos de Uso

| Original (`manager_user_security`) | Novo (`usersecurity`) | Status |
|------------------------------------|----------------------|--------|
| Não existia como componente separado | `application.usecase.AuthenticationUseCase` | ✅ Implementado |
| Outros casos de uso específicos | Implementações específicas pendentes | ❌ Pendente |

## Principais Componentes Pendentes

- [ ] Security Config (JwtAuthenticationFilter, SecurityConfig)
- [ ] Autenticação (JwtService, AuthenticationService)
- [ ] Avaliação de Permissões (PermissionEvaluator)
- [ ] Controladores para outros recursos
- [ ] DTOs para outros recursos
- [ ] Melhorias no tratamento de exceções
- [ ] Testes unitários e de integração

## Principais Componentes Pendentes

A seguir estão os principais componentes que ainda precisam ser implementados na nova arquitetura:

1. **Controladores da API REST**: 
   - [ ] AuthenticationController
   - [ ] UserController
   - [ ] RoleController
   - [ ] ResourceController
   - [ ] PermissionController
   - [ ] Outros controladores específicos de negócio

2. **DTOs da API**:
   - [ ] AuthenticationRequest
   - [ ] AuthenticationResponse
   - [ ] TokenValidationResponse
   - [ ] UserRequest/Response
   - [ ] RoleRequest/Response
   - [ ] ResourceRequest/Response
   - [ ] PermissionRequest/Response
   - [ ] Outros DTOs específicos

3. **Entidades de Domínio Específicas**:
   - [ ] Empresa/Company
   - [ ] Planta/Plant
   - [ ] Silo
   - [ ] SiloModulo/SiloModule
   - [ ] TipoSilo/SiloType
   - [ ] Medicao/Measurement
   - [ ] Logger
   - [ ] Pendencia/Pendency
   - [ ] Firmware

4. **Implementações de Repositórios Específicos**:
   - [ ] Implementações concretas para repositórios específicos de negócio

5. **Serviços Específicos**:
   - [ ] Serviços relacionados a recursos específicos de negócio

6. **Migração de Dados**:
   - [ ] Estratégia para migração de dados entre estruturas antigas e novas

## Próximos Passos Recomendados

1. **Implementar interfaces da API**: Completar os controladores REST para os principais recursos.
2. **Criar DTOs para interfaces de API**: Desenvolver DTOs específicos para request/response.
3. **Implementar entidades de negócio específicas**: Criar as entidades de domínio e persistência para recursos de negócio pendentes.
4. **Implementar casos de uso específicos**: Criar casos de uso para as operações de negócio.
5. **Testar componentes implementados**: Desenvolver testes unitários e de integração.
6. **Realizar integração com front-end**: Garantir compatibilidade com interfaces de usuário existentes.
7. **Documentar API**: Completar a documentação OpenAPI/Swagger.
8. **Finalizar migração**: Remover código antigo após migração completa.
