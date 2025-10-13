# Configuração do Ambiente de Desenvolvimento

Este documento fornece instruções detalhadas para configurar o ambiente de desenvolvimento para o projeto Manager User Security.

## Requisitos do Sistema

- JDK 17 ou superior
- Maven 3.8 ou superior
- Docker e Docker Compose (opcional, para contêineres)
- PostgreSQL 16 (standalone ou via Docker)
- IDE Java (recomendado: IntelliJ IDEA ou VS Code)

## Configuração do Ambiente Local

### 1. Configurando o JDK

Certifique-se de ter o JDK 17 instalado e configurado:

```bash
# Verificar versão do JDK
java -version

# A saída deve mostrar algo como:
# openjdk version "17.0.x" 2023-xx-xx
```

### 2. Configurando o Maven

Verifique se o Maven está corretamente instalado:

```bash
# Verificar versão do Maven
mvn -version

# A saída deve mostrar algo como:
# Apache Maven 3.8.x
```

### 3. Configuração do Banco de Dados

#### Opção 1: PostgreSQL via Docker (Recomendado)

```bash
# Iniciar o PostgreSQL usando Docker Compose
docker-compose up -d db

# Verificar se o contêiner está em execução
docker ps
```

O PostgreSQL estará disponível em `localhost:5455` com as seguintes credenciais:
- Database: `silo`
- Username: `admin`
- Password: `admin`

#### Opção 2: PostgreSQL Standalone

Se preferir instalar o PostgreSQL diretamente:

1. Baixe e instale o PostgreSQL 16 de [postgresql.org](https://www.postgresql.org/download/)
2. Crie um banco de dados chamado `silo`
3. Crie um usuário `admin` com senha `admin` e conceda todos os privilégios no banco `silo`

```sql
CREATE DATABASE silo;
CREATE USER admin WITH ENCRYPTED PASSWORD 'admin';
GRANT ALL PRIVILEGES ON DATABASE silo TO admin;
```

### 4. Configuração do Projeto

Clone o repositório e configure:

```bash
# Clonar o repositório
git clone https://github.com/ocoelhogabriel/manager-user-security.git

# Entrar no diretório do projeto
cd manager-user-security

# Instalar dependências
./mvnw clean install -DskipTests
```

### 5. Configurações de Ambiente

Crie um arquivo `.env` na raiz do projeto para configurar variáveis de ambiente:

```properties
# Configurações do JWT
JWT_SECRET=sua-chave-secreta-jwt

# Configurações de Banco de Dados (opcional, se diferente do padrão)
DB_URL=jdbc:postgresql://localhost:5455/silo
DB_USERNAME=admin
DB_PASSWORD=admin

# Perfil Spring
SPRING_PROFILES_ACTIVE=dev
```

## Executando o Projeto

### Utilizando Maven

```bash
# Executar com implementação original
./mvnw spring-boot:run

# Executar com nova implementação (Clean Architecture)
./mvnw spring-boot:run -Dspring-boot.run.profiles=clean
```

### Utilizando Docker Compose

```bash
# Iniciar toda a aplicação (banco de dados + aplicação)
docker-compose up -d

# Parar a aplicação
docker-compose down
```

### Executando em Modo Debug

```bash
# Debug via Maven
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
```

Em seguida, conecte seu IDE ao processo de debug na porta 5005.

## Configuração da IDE

### IntelliJ IDEA

1. Abra o projeto: `File > Open > {caminho-do-projeto}`
2. Configure o JDK 17: `File > Project Structure > Project > SDK`
3. Configure o Maven: `File > Settings > Build, Execution, Deployment > Build Tools > Maven`
4. Importe as dependências Maven: Clique no ícone do Maven no painel lateral direito e selecione "Reload All Maven Projects"

### VS Code

1. Instale as extensões:
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - Lombok Annotations Support

2. Abra a pasta do projeto
3. Configure o JDK: Pressione `Ctrl+Shift+P`, digite "Java: Configure Java Runtime"
4. Configure o Maven settings.xml se necessário

## Perfis de Configuração

O projeto suporta vários perfis Spring para diferentes ambientes:

- `dev` - Configurações para desenvolvimento local
- `test` - Configurações para execução de testes
- `clean` - Nova implementação com Clean Architecture
- `dev-clean` - Desenvolvimento com nova arquitetura
- `prod` - Configurações de produção

### Ativando Perfis

Via linha de comando:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev,clean
```

Via Docker:
```bash
docker-compose -f docker-compose.yml -f docker-compose.clean.yml up -d
```

## Testes

### Executando Todos os Testes

```bash
./mvnw test
```

### Executando Testes Específicos

```bash
# Executar testes de unidade
./mvnw test -Dtest="*UnitTest"

# Executar testes de integração
./mvnw test -Dtest="*IntegrationTest"
```

## Swagger/OpenAPI

A documentação da API está disponível em:
- Implementação original: `http://localhost:8092/manager_user_security/swagger-ui/index.html`
- Nova implementação: `http://localhost:8092/manager_user_security/swagger-ui/index.html?configUrl=/manager_user_security/v3/api-docs/swagger-config-clean`

## Monitoramento e Atuadores

Os atuadores Spring Boot estão disponíveis em:
```
http://localhost:8092/manager_user_security/actuator
```

## Resolução de Problemas Comuns

### Erro de Conexão com Banco de Dados

**Sintoma**: Erro "Unable to connect to database"

**Solução**: 
1. Verifique se o PostgreSQL está em execução
2. Confirme as credenciais em `application.properties`
3. Verifique a porta (5455 para Docker, 5432 para instalação padrão)

### Erro de Token JWT Inválido

**Sintoma**: Erro "Invalid token" em chamadas API autenticadas

**Solução**:
1. Verifique se o token não expirou
2. Confirme se a variável de ambiente `JWT_SECRET` está configurada corretamente
3. Teste com o endpoint `/api/autenticacao/v1/validate` para validar o token

### Erros de Build Maven

**Sintoma**: Falha ao compilar o projeto

**Solução**:
1. Limpe o cache Maven: `./mvnw clean`
2. Atualize as dependências: `./mvnw dependency:purge-local-repository`
3. Verifique conflitos de dependências: `./mvnw dependency:tree`