# Implementação de Testes Unitários

Este documento descreve os testes unitários implementados no projeto Manager User Security.

## Estrutura de Testes

Os testes foram organizados seguindo a mesma estrutura da aplicação, divididos em:

```
src/test/java/com/ocoelhogabriel/manager_user_security/unit/
  ├── application/
  │   ├── AuthenticationUseCaseTest.java
  │   └── UrlValidationServiceTest.java
  ├── domain/
  │   └── ...
  ├── infrastructure/
  │   └── security/
  │       ├── JwtTokenProviderTest.java
  │       ├── JwtAuthenticationFilterTest.java
  │       ├── UrlPathMatcherTest.java
  │       ├── PermissionEvaluatorTest.java
  │       └── SecurityConfigTest.java
  ├── interfaces/
  │   └── AuthenticationControllerTest.java
  └── TestConfig.java
```

## Testes Implementados

### Camada de Infraestrutura

1. **JwtTokenProviderTest**
   - Testa a geração de tokens JWT
   - Testa a validação de tokens
   - Testa a extração de informações do token
   - Testa a renovação de tokens

2. **JwtAuthenticationFilterTest**
   - Testa o fluxo de autenticação com token válido
   - Testa o comportamento com token expirado
   - Testa o comportamento com token inválido
   - Testa a validação de permissões

3. **UrlPathMatcherTest**
   - Testa a validação de formatos de URL
   - Testa a extração de parâmetros de ID
   - Testa o reconhecimento de recursos a partir de URLs

4. **PermissionEvaluatorTest**
   - Testa a verificação de permissões por método HTTP
   - Testa o comportamento com diferentes configurações de permissão
   - Testa casos de exceção (valores nulos)

5. **SecurityConfigTest**
   - Testa a criação do bean PasswordEncoder
   - Testa a criação do bean AuthenticationManager

### Camada de Aplicação

1. **AuthenticationUseCaseTest**
   - Testa autenticação com credenciais válidas
   - Testa autenticação com credenciais inválidas
   - Testa validação de tokens
   - Testa renovação de tokens
   - Testa obtenção do usuário atual

2. **UrlValidationServiceTest**
   - Testa identificação de URLs públicas
   - Testa validação de formato de URLs
   - Testa extração de informações de recursos a partir de URLs
   - Testa mapeamento de métodos HTTP para ações

### Camada de Interfaces

1. **AuthenticationControllerTest**
   - Testa endpoint de autenticação
   - Testa endpoint de validação de token
   - Testa endpoint de renovação de token
   - Testa endpoint de informações do usuário atual

## Configuração para Testes

Foi criada uma classe `TestConfig` que fornece beans comuns utilizados nos testes:

- PasswordEncoder
- ObjectMapper configurado com JavaTimeModule
- UserDetailsService para testes

## Cobertura de Código

Foi adicionado o plugin JaCoCo para análise de cobertura de código, que gera relatórios na fase de package do Maven.

Para executar os testes e gerar o relatório de cobertura:

```
mvn clean test jacoco:report
```

Os relatórios de cobertura podem ser encontrados em:
`target/site/jacoco/index.html`