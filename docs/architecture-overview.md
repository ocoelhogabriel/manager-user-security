# Arquitetura do Sistema

Este documento apresenta uma visão geral da arquitetura do sistema Manager User Security, tanto na implementação original quanto na nova implementação baseada em Clean Architecture.

## Visão Geral da Arquitetura

### Arquitetura Original

A implementação original do Manager User Security segue uma arquitetura em camadas tradicional:

```
+---------------------------+
|      Controladores        |
+---------------------------+
|         Serviços          |
+---------------------------+
|       Repositórios        |
+---------------------------+
|     Entidades/Modelos     |
+---------------------------+
|    Banco de Dados (SQL)   |
+---------------------------+
```

**Características:**
- Camadas com fronteiras não claramente definidas
- Dependências diretas entre camadas adjacentes
- Regras de negócio distribuídas entre serviços e entidades
- Acoplamento entre lógica de negócios e frameworks

### Nova Arquitetura (Clean Architecture)

A nova implementação segue os princípios de Clean Architecture:

```
                 +------------------------------------------+
                 |              Interfaces                  |
                 | (API REST, Controllers, DTOs)            |
                 +------------------------------------------+
                       |                  ^
                       v                  |
+------------------------------------------+------------------------------------------+
|                                Infrastructure                                       |
| (Implementações de Repositórios, Serviços Externos, Segurança, Configurações)      |
+------------------------------------------+------------------------------------------+
                       |                  ^
                       v                  |
                 +------------------------------------------+
                 |             Aplicação                    |
                 | (Casos de Uso, Serviços de Aplicação)   |
                 +------------------------------------------+
                       |                  ^
                       v                  |
                 +------------------------------------------+
                 |              Domínio                     |
                 | (Entidades, Regras de Negócio)          |
                 +------------------------------------------+
```

**Características:**
- Camadas concêntricas com dependências dirigidas para o centro
- Camada de Domínio independente de frameworks e detalhes externos
- Inversão de dependência usando interfaces
- Separação clara entre regras de negócio e detalhes de implementação

## Estrutura de Pacotes

### Estrutura Original
```
com.ocoelhogabriel.manager_user_security
├── config                    # Configurações (SecurityConfig, JWTAuthFilter)
├── controller                # Controladores REST
├── exception                 # Exceções e handlers
├── handler                   # Handlers (PermissaoHandler, URLValidator)
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

### Estrutura Clean Architecture
```
com.ocoelhogabriel.usersecurity
├── domain                      # Camada de Domínio
│   ├── entity                  # Entidades de domínio
│   ├── exception               # Exceções de domínio
│   ├── repository              # Interfaces de repositório
│   ├── service                 # Interfaces de serviço
│   └── valueobject             # Objetos de valor
│
├── application                 # Camada de Aplicação
│   ├── dto                     # DTOs internos
│   ├── exception               # Exceções de aplicação
│   ├── service                 # Implementações de serviços
│   │   └── impl                # Implementações concretas
│   └── usecase                 # Casos de uso específicos
│
├── infrastructure              # Camada de Infraestrutura
│   ├── config                  # Configurações gerais
│   ├── persistence             # Implementações de persistência
│   │   ├── config              # Configurações de persistência
│   │   ├── entity              # Entidades JPA
│   │   ├── mapper              # Mapeadores entre entidades JPA e domínio
│   │   └── repository          # Implementações JPA de repositórios
│   │       └── impl            # Implementações concretas
│   └── security                # Implementações de segurança
│       ├── config              # Configurações de segurança
│       ├── filter              # Filtros de segurança
│       ├── handler             # Handlers de segurança
│       ├── jwt                 # Serviços JWT
│       ├── permission          # Avaliação de permissões
│       └── service             # Serviços de segurança
│
└── interfaces                  # Camada de Interfaces
    ├── advice                  # Handlers de exceção globais
    └── api                     # API REST
        ├── controller          # Controladores REST
        └── dto                 # DTOs de API
            ├── request         # DTOs de requisição
            └── response        # DTOs de resposta
```

## Componentes Principais

### Autenticação e Autorização

#### Implementação Original
- `JWTAuthFilter`: Intercepta requisições, valida tokens JWT e configura autenticação no contexto do Spring Security
- `SecurityConfig`: Configurações gerais de segurança, definição de URLs públicas e privadas
- `PermissaoHandler`: Verifica permissões de usuário para determinados recursos
- `URLValidator`: Valida URLs e determina permissões necessárias

#### Implementação Clean Architecture
- `JwtAuthenticationFilter`: Filtro de autenticação JWT aprimorado
- `WebSecurityConfig`: Configuração de segurança baseada em componentes
- `CustomPermissionEvaluator`: Avaliador de permissões baseado em expressões de segurança
- `JwtService`: Serviço para gerenciamento de tokens JWT
- `UserDetailsServiceImpl`: Implementação do serviço de detalhes do usuário

### Entidades de Domínio

#### Implementação Original
Entidades acopladas ao framework de persistência (JPA):
- `Usuario.java`
- `Perfil.java`
- `Recurso.java`
- `Permissao.java`
- etc.

#### Implementação Clean Architecture
Entidades de domínio puras, sem anotações de framework:
- `domain.entity.User.java`
- `domain.entity.Role.java`
- `domain.entity.Resource.java`
- `domain.entity.Permission.java`
- etc.

Entidades de persistência separadas:
- `infrastructure.persistence.entity.UserEntity.java`
- `infrastructure.persistence.entity.RoleEntity.java`
- etc.

### Repositórios

#### Implementação Original
Interfaces Spring Data JPA diretamente acopladas ao framework:
- `UsuarioRepository extends JpaRepository<Usuario, Long>`
- `PerfilRepository extends JpaRepository<Perfil, Long>`
- etc.

#### Implementação Clean Architecture
Interfaces de repositório no domínio:
- `domain.repository.UserRepository`
- `domain.repository.RoleRepository`
- etc.

Implementações na camada de infraestrutura:
- `infrastructure.persistence.repository.UserRepositoryImpl`
- `infrastructure.persistence.repository.RoleRepositoryImpl`
- etc.

### API REST

#### Implementação Original
Controladores diretamente expostos com lógica de negócios:
- `AuthenticationController`
- `UsuarioController`
- etc.

#### Implementação Clean Architecture
Controladores finos que delegam para casos de uso:
- `interfaces.api.rest.AuthenticationController`
- `interfaces.api.rest.UserController`
- etc.

DTOs específicos para API:
- `interfaces.api.rest.dto.request.LoginRequest`
- `interfaces.api.rest.dto.response.AuthenticationResponse`
- etc.

## Benefícios da Nova Arquitetura

1. **Testabilidade**: Componentes podem ser testados isoladamente com mock objects
2. **Manutenibilidade**: Mudanças em uma camada têm impacto limitado em outras
3. **Flexibilidade**: Frameworks externos podem ser substituídos com facilidade
4. **Clareza**: Responsabilidades bem definidas para cada componente
5. **Escalabilidade**: Facilita o desenvolvimento paralelo por múltiplas equipes

## Desafios da Migração

1. **Duplicação Temporária**: Duas implementações coexistindo durante a migração
2. **Complexidade Inicial**: Estrutura mais elaborada com mais classes e interfaces
3. **Curva de Aprendizado**: Equipe precisa se adaptar aos novos padrões
4. **Mapeamento Adicional**: Necessidade de mapear entre entidades de domínio e persistência

## Regras de Design

1. **Regra de Dependência**: Dependências sempre apontam para dentro (em direção ao domínio)
2. **Entidades Puras**: Entidades de domínio não dependem de frameworks externos
3. **Segregação de Interfaces**: Interfaces específicas para cada camada e necessidade
4. **Injeção de Dependência**: Componentes recebem suas dependências, não as criam

## Considerações de Implementação

1. **Mapeadores**: Utilizar mapeadores explícitos entre diferentes representações de entidades
2. **DTOs**: Usar DTOs específicos para comunicação entre camadas
3. **Exceções**: Hierarquia de exceções específicas para cada camada
4. **Validação**: Validações de entrada na camada de interfaces, validações de regras de negócio no domínio