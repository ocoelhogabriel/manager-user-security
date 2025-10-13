# Manager User Security

Sistema de Gerenciamento de Usuários e Segurança seguindo Clean Architecture.

## 🔒 Visão Geral

Este projeto implementa uma solução completa para autenticação, autorização e gerenciamento de usuários baseada em Spring Boot e JWT. A arquitetura segue os princípios de Clean Architecture para garantir uma separação clara de responsabilidades e facilitar a manutenção e evolução do sistema.

## 📚 Documentação

Para mais detalhes sobre o projeto, consulte a documentação completa na pasta [docs](./docs/).

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

## 📄 Licença

Copyright 2023-2025 Gabriel O. Coelho. Todos os direitos reservados.