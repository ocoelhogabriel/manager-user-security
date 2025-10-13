# Instruções para Migração de Configuração

Este documento fornece instruções para migrar da configuração antiga para a nova configuração baseada em Clean Architecture.

## Ativando a Nova Configuração

Para ativar a nova configuração, adicione o seguinte parâmetro ao executar a aplicação:

```shell
java -jar manager-user-security.war --spring.profiles.active=clean
```

Ou ao executar com Maven:

```shell
./mvnw spring-boot:run -Dspring-boot.run.profiles=clean
```

## Alternativa: Substituir Configurações Antigas

Se preferir, você pode substituir completamente o arquivo `application.properties` original pelo novo arquivo `application-clean.properties`, renomeando-o para `application.properties`.

## Novas Propriedades Adicionadas

1. **Configurações de Pool de Conexão**:
   - `spring.datasource.hikari.maximum-pool-size=10`
   - `spring.datasource.hikari.minimum-idle=5`
   - `spring.datasource.hikari.idle-timeout=30000`

2. **Configurações de Compressão de Resposta**:
   - `server.compression.enabled=true`
   - `server.compression.mime-types=text/html,text/xml,text/plain,text/css,application/javascript,application/json`
   - `server.compression.min-response-size=1024`

3. **Configurações de JPA Aprimoradas**:
   - `spring.jpa.properties.hibernate.format_sql=true`
   - `spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect`
   - `spring.jpa.open-in-view=false`

4. **Configurações de Logging**:
   - `logging.level.org.springframework.security=INFO`
   - `logging.level.com.ocoelhogabriel.manager_user_security=INFO`

## Propriedades Mantidas

1. Configurações de JWT:
   - `api.security.token.secret=${JWT_SECRET:my-secret-key}`
   - `api.security.expiration.time.minutes=1440`

2. Configurações de Banco de Dados:
   - `spring.datasource.url=jdbc:postgresql://localhost:5432/silo`
   - `spring.datasource.username=admin`
   - `spring.datasource.password=admin`

## Configurações de Ambiente de Desenvolvimento vs. Produção

Para ambientes de desenvolvimento e produção diferentes, crie arquivos adicionais:

- `application-dev-clean.properties` - Para desenvolvimento
- `application-prod-clean.properties` - Para produção

E ative-os usando o perfil apropriado:

```shell
java -jar manager-user-security.war --spring.profiles.active=prod-clean
```