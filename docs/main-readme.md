````markdown
# Manager User Security

Sistema de Gerenciamento de Usuários e Segurança seguindo Clean Architecture.

## 🔒 Visão Geral

Este projeto implementa uma solução completa para autenticação, autorização e gerenciamento de usuários baseada em Spring Boot e JWT. A arquitetura segue os princípios de Clean Architecture para garantir uma separação clara de responsabilidades e facilitar a manutenção e evolução do sistema.

## 🏛️ Arquitetura

O projeto é organizado seguindo a Clean Architecture com as seguintes camadas:

### Camadas Principais

- **Domain**: Contém as entidades de negócio, interfaces de repositórios e regras de negócio
- **Application**: Implementa casos de uso e orquestra fluxos de negócio
- **Infrastructure**: Adapta tecnologias externas (banco de dados, serviços, etc.)
- **Interfaces**: Gerencia a comunicação externa (API REST, serviços web, etc.)

### Pacotes Principais

```
src/main/java/com/ocoelhogabriel/manager_user_security/
├── application/     ← Serviços de aplicação e casos de uso
├── config/          ← Configurações (JWT, Security, etc.)
├── domain/          ← Entidades e regras de negócio
├── exception/       ← Tratamento global de exceções
├── infrastructure/  ← Implementações de repositórios, adapters, etc.
├── interfaces/      ← Controllers REST e DTOs
└── utils/           ← Utilitários
```

## 🚀 Iniciando o Projeto

### Requisitos

- Java 17+
- Maven 3.8+
- Docker e Docker Compose (para ambiente containerizado)
- PostgreSQL (local ou container)

### Execução Local

```bash
# Compilar o projeto
./mvnw clean install

# Executar localmente
./mvnw spring-boot:run
```

### Execução com Docker

```bash
# Construir e iniciar os containers
docker compose up --build -d

# Verificar logs
docker compose logs -f backend
```

## 🔧 Desenvolvimento

### Convenções de Código

- **Nomenclatura**: Nomes de classes em CamelCase, métodos em camelCase, constantes em UPPER_SNAKE_CASE
- **Linguagem**: Usar inglês para código e comentários técnicos
- **Testes**: Manter cobertura mínima de 80% para código de produção
- **Logging**: Usar LogManager para logs estruturados e contextualizados

### Padrões de Design

- **Builder Pattern**: Para construção de objetos complexos
- **Repository Pattern**: Para acesso a dados
- **Adapter Pattern**: Para integração com sistemas externos
- **DTO Pattern**: Para transferência de dados entre camadas
- **Service Layer**: Para orquestração de operações de negócios

### Melhores Práticas

1. **Segurança**:
   - Nunca expor senhas em logs ou toString()
   - Sempre validar dados de entrada
   - Utilizar HTTPS em produção
   - Implementar princípio de menor privilégio

2. **Performance**:
   - Preferir fetch LAZY para relacionamentos JPA
   - Usar paginação para grandes conjuntos de dados
   - Configurar adequadamente o pool de conexões

3. **Qualidade**:
   - Executar análise estática antes de commits
   - Manter cobertura de testes
   - Seguir princípios SOLID

## 🔄 Evolução Planejada

### Pendências Conhecidas

1. **Resolver Dependências Circulares**:
   - Ver arquivo `config/circular-dependencies.properties`

2. **Padronizar Nomenclatura**:
   - Unificar pacotes duplicados (`use_cases`/`usecases`)
   - Usar inglês em todos os pacotes

3. **Melhorar Observabilidade**:
   - Implementar rastreamento distribuído com Micrometer
   - Configurar dashboard de métricas

### Roadmap

- [x] Autenticação JWT
- [x] Gerenciamento de Usuários
- [x] Controle de Acesso Baseado em Perfis
- [ ] Autenticação 2FA (planejado)
- [ ] Login via OAuth2 (planejado)
- [ ] Módulo de Auditoria Completo (planejado)

## 📚 Documentação

A documentação da API está disponível em:

- Swagger UI: [http://localhost:8080/manager_user_security/swagger-ui.html](http://localhost:8080/manager_user_security/swagger-ui.html)
- OpenAPI JSON: [http://localhost:8080/manager_user_security/v3/api-docs](http://localhost:8080/manager_user_security/v3/api-docs)

## 🧪 Testes

```bash
# Executar todos os testes
./mvnw test

# Executar testes com cobertura
./mvnw test jacoco:report

# Executar testes de performance
./mvnw test -P performance
```

## 🔐 Segurança

Para relatar vulnerabilidades de segurança, entre em contato com a equipe através do email: [seguranca@example.com](mailto:seguranca@example.com)

## 📄 Licença

Copyright 2023-2025 Gabriel O. Coelho. Todos os direitos reservados.
````