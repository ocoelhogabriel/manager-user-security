# Mapa de Migração para Clean Architecture

## Estrutura do Projeto

### Estrutura Original (`manager_user_security`)
```
com.ocoelhogabriel.manager_user_security
├── config        # Configurações gerais e de segurança
├── controller    # Controladores REST
├── model         # Modelos/entidades 
├── repository    # Repositórios JPA
├── services      # Serviços de negócio
├── utils         # Classes utilitárias
└── handler       # Handlers específicos
```

### Nova Estrutura Clean Architecture (`usersecurity`)
```
com.ocoelhogabriel.usersecurity
├── domain            # Camada de domínio
│   ├── entities      # Entidades de negócio
│   ├── services      # Serviços de domínio
│   ├── repositories  # Interfaces de repositório
│   └── value_objects # Value Objects
├── application       # Camada de aplicação
│   ├── dtos          # DTOs
│   ├── services      # Serviços de aplicação
│   └── use_cases     # Casos de uso
├── infrastructure    # Camada de infraestrutura
│   ├── adapters      # Adaptadores
│   ├── configuration # Configurações
│   ├── entities      # Entidades de persistência
│   └── security      # Implementação de segurança
└── interfaces        # Camada de interface
    └── web           # Controladores REST
```

## Mapeamento de Classes

### Componentes de Segurança

| Original | Nova Implementação | Observações |
|----------|-------------------|-------------|
| `SecurityConfig` | `SecurityConfiguration` | Refatorado para usar autorização baseada em anotações |
| `JWTAuthFilter` | `JwtAuthenticationFilter` | Melhor separação de responsabilidades |
| `PermissaoHandler` | Removido | Substituído por anotações `@PreAuthorize` |
| `URLValidator` | Removido | Validação incorporada ao processo de autorização |

### Autenticação

| Original | Nova Implementação | Observações |
|----------|-------------------|-------------|
| `AuthenticationController` | `AuthenticationController` | API mais clara e documentada |
| `AuthenticationService` | `AuthenticationUseCase` | Implementação como caso de uso |
| `TokenProvider` | `JwtTokenProvider` | Melhor isolamento de responsabilidades |

### Entidades

| Original | Nova Implementação | Observações |
|----------|-------------------|-------------|
| `Usuario` | `User` | Domínio puro sem anotações JPA |
| `Papel` | `Role` | Domínio puro sem anotações JPA |
| `Permissao` | `Permission` | Domínio puro sem anotações JPA |
| `Recurso` | `Resource` | Domínio puro sem anotações JPA |
| `Empresa` | `Company` | Domínio puro sem anotações JPA |
| `Planta` | `Plant` | Domínio puro sem anotações JPA |
| `Silo` | Removido | Removido do escopo do projeto |
| `TipoSilo` | Removido | Removido do escopo do projeto |
| `ModuloSilo` | Removido | Removido do escopo do projeto |
| `Medicao` | Removido | Removido do escopo do projeto |
| `Firmware` | Removido | Removido do escopo do projeto |
| `Pendencia` | Removido | Removido do escopo do projeto |

### Repositórios

| Original | Nova Implementação | Observações |
|----------|-------------------|-------------|
| `UsuarioRepository` | `UserRepository` (interface) + `UserRepositoryImpl` | Inversão de dependência |
| `PapelRepository` | `RoleRepository` (interface) + `RoleRepositoryImpl` | Inversão de dependência |
| `PermissaoRepository` | `PermissionRepository` (interface) + `PermissionRepositoryImpl` | Inversão de dependência |
| `EmpresaRepository` | `CompanyRepository` (interface) + `CompanyRepositoryImpl` | Inversão de dependência |
| `PlantaRepository` | `PlantRepository` (interface) + `PlantRepositoryImpl` | Inversão de dependência |
| `SiloRepository` | Removido | Removido do escopo do projeto |
| `TipoSiloRepository` | Removido | Removido do escopo do projeto |
| `ModuloSiloRepository` | Removido | Removido do escopo do projeto |
| `MedicaoRepository` | Removido | Removido do escopo do projeto |
| `FirmwareRepository` | Removido | Removido do escopo do projeto |
| `PendenciaRepository` | Removido | Removido do escopo do projeto |

### Controladores

| Original | Nova Implementação | Observações |
|----------|-------------------|-------------|
| `UsuarioController` | `UserController` | Desacoplado usando casos de uso |
| `PapelController` | `RoleController` | Desacoplado usando casos de uso |
| `PermissaoController` | `PermissionController` | Desacoplado usando casos de uso |
| `RecursoController` | `ResourceController` | Desacoplado usando casos de uso |
| `EmpresaController` | `CompanyController` | Desacoplado usando casos de uso |
| `PlantaController` | `PlantController` | Desacoplado usando casos de uso |
| `SiloController` | Removido | Removido do escopo do projeto |
| `TipoSiloController` | Removido | Removido do escopo do projeto |
| `ModuloSiloController` | Removido | Removido do escopo do projeto |
| `MedicaoController` | Removido | Removido do escopo do projeto |
| `FirmwareController` | Removido | Removido do escopo do projeto |
| `PendenciaController` | Removido | Removido do escopo do projeto |

## Benefícios da Nova Arquitetura

1. **Testabilidade**:
   - Domínio puro testável sem dependências externas
   - Casos de uso isolados para testes unitários precisos
   - Mocks e stubs facilmente utilizáveis através de interfaces

2. **Flexibilidade**:
   - Mudanças de infraestrutura (banco de dados, frameworks) não afetam o domínio
   - Possibilidade de múltiplas interfaces de usuário (API REST, GraphQL, etc.)
   - Fácil adição de novos casos de uso sem modificação do domínio

3. **Manutenibilidade**:
   - Separação clara de responsabilidades
   - Menor acoplamento entre componentes
   - Dependências apontam para o centro (domínio)
   - Código mais fácil de entender e modificar

4. **Segurança**:
   - Melhor controle sobre validação de entrada
   - Regras de autorização claras e centralizadas
   - Melhor gestão de tokens e credenciais

## Desafios Encontrados

1. **Migração de Dados**: 
   - Estruturas de dados diferentes entre as implementações
   - Necessidade de scripts de migração

2. **Curva de Aprendizado**: 
   - Maior complexidade inicial da arquitetura
   - Necessidade de entendimento dos princípios de Clean Architecture

3. **Overhead de Código**:
   - Mais classes e interfaces no início
   - Maior quantidade de mapeamentos entre DTOs e entidades

## Conclusão

A migração para Clean Architecture, embora desafiadora inicialmente, proporciona uma base sólida para o crescimento futuro do sistema. A remoção de componentes não utilizados (Silo, TipoSilo, Medicao, etc.) permitiu um foco maior nas funcionalidades essenciais, resultando em uma implementação mais enxuta e direcionada.