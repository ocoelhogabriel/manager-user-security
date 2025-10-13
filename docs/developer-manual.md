# Manual do Desenvolvedor

Este manual fornece orientações para desenvolvedores que trabalham no projeto Manager User Security.

## Configuração do Ambiente de Desenvolvimento

### Pré-requisitos

- JDK 17 ou superior
- Maven 3.8 ou superior
- Docker e Docker Compose
- IDE (recomendado: IntelliJ IDEA, Eclipse ou VS Code com extensões Java)
- Git

### Configuração do IDE

#### IntelliJ IDEA

1. Importe o projeto como projeto Maven
2. Configure o JDK 17
3. Instale os plugins recomendados:
   - SonarLint
   - Checkstyle-IDEA
   - Save Actions
4. Importe as configurações de estilo:
   - `File > Settings > Editor > Code Style > Import Scheme > IntelliJ IDEA code style XML`
   - Selecione o arquivo `config/intellij-code-style.xml`

#### Eclipse

1. Importe o projeto como projeto Maven existente
2. Configure o JDK 17
3. Instale os plugins recomendados:
   - SonarLint
   - Checkstyle
   - Eclipse Code Formatter
4. Importe as configurações de estilo:
   - `Window > Preferences > Java > Code Style > Formatter > Import`
   - Selecione o arquivo `config/eclipse-code-style.xml`

#### VS Code

1. Abra a pasta do projeto
2. Instale as extensões recomendadas:
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - SonarLint
   - Checkstyle for Java
3. As configurações já estão definidas em `.vscode/settings.json`

### Configuração Local

1. Clone o repositório:

```bash
git clone https://github.com/OCoelhoGabriel/manager_user_security.git
cd manager_user_security
```

2. Configure as variáveis de ambiente locais criando um arquivo `.env` na raiz do projeto:

```properties
POSTGRES_DB=manager_user_security
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
JWT_SECRET=your_jwt_secret_key
JWT_EXPIRATION=86400000
```

3. Inicialize o banco de dados com Docker:

```bash
docker-compose up -d postgres
```

## Construção e Execução

### Compilação

```bash
./mvnw clean compile
```

### Execução Local

```bash
./mvnw spring-boot:run
```

### Execução com Docker

```bash
docker-compose up --build
```

### Execução de Testes

```bash
# Todos os testes
./mvnw test

# Testes unitários
./mvnw test -Dtest=*Test

# Testes de integração
./mvnw test -Dtest=*IT

# Com cobertura de código
./mvnw test jacoco:report
```

## Fluxo de Desenvolvimento

### Branches

Seguimos o modelo GitFlow com as seguintes branches:

- `main`: Código de produção estável
- `develop`: Código em desenvolvimento ativo
- `feature/*`: Novas funcionalidades
- `bugfix/*`: Correções de bugs
- `release/*`: Preparação para release
- `hotfix/*`: Correções urgentes em produção

### Criando uma Nova Feature

```bash
git checkout develop
git pull
git checkout -b feature/nome-da-feature
# Desenvolva a funcionalidade
git add .
git commit -m "feat: descrição da funcionalidade"
git push -u origin feature/nome-da-feature
# Crie um pull request para develop
```

### Formatação de Código

Antes de submeter código, execute o script de formatação:

```bash
# Windows
.\scripts\format-code.cmd

# Unix/Linux/Mac
./scripts/format-code.sh
```

### Verificação de Código

Execute as verificações de qualidade antes de submeter código:

```bash
# Windows
.\scripts\verify-code.cmd

# Unix/Linux/Mac
./scripts/verify-code.sh
```

## Estrutura do Projeto

O projeto segue os princípios da Clean Architecture:

```
src/main/java/com/ocoelhogabriel/manager_user_security/
├── application/     ← Application services and use cases
├── config/          ← Security configurations (JWT, Web Security)
├── controller/      ← REST API endpoints
├── domain/          ← Domain entities and business logic
├── exception/       ← Global exception handling
├── handler/         ← Business logic handlers
├── infrastructure/  ← External services implementation
├── interfaces/      ← Interface definitions and API contracts
└── utils/           ← Utility classes
```

### Camadas Arquiteturais

1. **Domain**: Entidades de negócio, regras de negócio e interfaces de repositório
2. **Application**: Casos de uso que orquestram as entidades
3. **Infrastructure**: Implementações concretas das interfaces do domínio
4. **Interfaces**: Controllers e DTOs para interagir com o mundo externo

## Documentação de API

A documentação da API está disponível via Swagger/OpenAPI:

- Desenvolvimento local: http://localhost:8080/swagger-ui.html
- Ambiente de teste: https://api-test.example.com/swagger-ui.html
- Ambiente de produção: https://api.example.com/swagger-ui.html

## Guia de Solução de Problemas

### Problemas Comuns

#### Falha na Conexão com o Banco de Dados

1. Verifique se o contêiner Docker do PostgreSQL está em execução:
   ```bash
   docker ps
   ```
2. Verifique as variáveis de ambiente no arquivo `.env`
3. Teste a conexão diretamente:
   ```bash
   psql -h localhost -U postgres -d manager_user_security
   ```

#### Problemas com JWT

1. Verifique se a variável `JWT_SECRET` está configurada corretamente
2. Verifique se o token não expirou (padrão: 24 horas)
3. Verifique se o token está sendo enviado no cabeçalho `Authorization` como `Bearer <token>`

#### Erros de Compilação

1. Verifique a versão do JDK (deve ser 17 ou superior)
2. Execute `./mvnw clean` para limpar artefatos de compilação anteriores
3. Verifique por dependências quebradas:
   ```bash
   ./mvnw dependency:tree
   ```

## Recursos Adicionais

- [Documentação de Arquitetura](../architecture/architecture-overview.md)
- [Decisões de Arquitetura (ADRs)](../architecture/adr/README.md)
- [Padrões de Codificação](../coding-standards/coding-standards.md)
- [Guia de Contribuição](../CONTRIBUTING.md)

## Contato

Para questões sobre o desenvolvimento, entre em contato com a equipe de desenvolvimento em dev@example.com.
