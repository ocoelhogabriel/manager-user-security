# Guia de Refatoração e Boas Práticas - manager-user-security

## 1. Visão Geral

O projeto `manager-user-security` possui uma base arquitetural sólida, utilizando uma abordagem em camadas que se inspira em princípios do Domain-Driven Design (DDD) e da Arquitetura Hexagonal (Ports and Adapters). O código é separado em camadas de domínio, aplicação, infraestrutura e interfaces, o que é uma excelente prática.

Este documento serve como um guia de referência para:
1.  **Mapear** a estrutura atual do projeto.
2.  **Identificar** inconsistências e pontos de melhoria.
3.  **Definir** um plano de ação claro para refatorar o código, aumentar a cobertura de testes e melhorar a manutenibilidade geral.

## 2. Mapeamento da Arquitetura Atual

A estrutura do projeto está dividida nas seguintes camadas lógicas:

#### `src/main/java/com/ocoelhogabriel/manager_user_security`

-   **Camada de Domínio (`domain`)**: O coração da aplicação. Contém a lógica de negócio principal, livre de dependências de frameworks externos.
    -   `entity`: As entidades de negócio (ex: `User`, `Role`, `Company`). Representam o estado e o comportamento do domínio.
    -   `repository`: As interfaces (Ports) que definem como o domínio acessa dados, abstraindo os detalhes de persistência (ex: `UserRepository`, `RoleRepository`).
    -   `service`: As interfaces para serviços de domínio que contêm lógica de negócio que não pertence a uma única entidade.
    -   `exception`: Exceções de negócio que representam estados inválidos do domínio (ex: `AuthenticationException`).
    -   `valueobject`: Objetos de valor imutáveis (ex: `HttpMethod`).

-   **Camada de Aplicação (`application`)**: Orquestra o domínio para executar casos de uso específicos.
    -   `usecase`: Interfaces que definem os casos de uso do sistema (ex: `AuthenticationUseCase`).
    -   `service`: **(Inconsistência)** Contém implementações de serviços. Algumas implementam interfaces do domínio, outras não. Muitas estão em um subpacote `impl`, o que é verboso.
    -   `dto`: Objetos de Transferência de Dados usados para comunicação interna entre camadas.
    -   `exception`: Exceções relacionadas ao fluxo da aplicação.

-   **Camada de Infraestrutura (`infrastructure`)**: Implementa as interfaces definidas pelo domínio e pela aplicação, lidando com preocupações externas (banco de dados, segurança, etc.).
    -   `persistence`: A implementação da persistência de dados.
        -   `adapter`: As implementações (Adapters) das interfaces de repositório do domínio (ex: `UserRepositoryAdapter`).
        -   `entity`: As entidades JPA (`@Entity`) que são mapeadas para o banco de dados.
        -   `repository`: As interfaces Spring Data JPA (ex: `UserJpaRepository`).
        -   `mapper`: Mapeadores para converter entre entidades de domínio e entidades JPA.
    -   `security`: **(Inconsistência)** Lógica de segurança, como configuração do Spring Security e manipulação de JWT. A estrutura está espalhada e duplicada (`auth/jwt`, `security/jwt`, `security/config`).
    -   `config`: Configurações gerais da aplicação, como `OpenApiConfig`.

-   **Camada de Interfaces (`interfaces`)**: O ponto de entrada da aplicação. Expõe a API REST e lida com as interações HTTP.
    -   `api` / `controller`: **(Inconsistência)** Contém os Controllers REST. A nomenclatura do pacote é redundante.
    -   `advice`: Handlers de exceção globais (`GlobalExceptionHandler`).
    -   `dto` / `mapper`: DTOs e mappers específicos para a camada de API, para converter objetos de domínio em respostas HTTP.

## 3. Princípios Orientadores da Refatoração

-   **Consistência**: Um padrão, um lugar. Todas as entidades, serviços e repositórios devem seguir a mesma estrutura de pacotes e nomenclatura.
-   **Testabilidade**: Código não testado é código legado. Toda nova lógica de negócio ou fluxo de aplicação deve ser acompanhada de testes unitários e/ou de integração.
-   **Clareza > Inteligência**: Escreva código que os outros (e você no futuro) possam entender facilmente. Evite complexidade desnecessária e nomes ambíguos.
-   **Separação de Responsabilidades (SoC)**: Reforce a separação entre as camadas. O domínio nunca deve conhecer a infraestrutura.

## 4. Plano de Ação Detalhado

### Fase 1: Limpeza e Padronização Estrutural

1.  **Consolidar Configuração de Segurança**
    -   **Objetivo**: Ter um único local para toda a configuração de segurança.
    -   **Passos**:
        1.  Analise o conteúdo de `infrastructure/security/SecurityConfig.java` e `infrastructure/security/config/SecurityConfig.java`.
        2.  Unifique-os em uma única classe `infrastructure/security/SecurityConfig.java`.
        3.  Mova o conteúdo de `infrastructure/auth/jwt/` para `infrastructure/security/jwt/`.
        4.  Refatore `JwtTokenProvider` e `JwtService` em um único serviço coeso, por exemplo, `JwtManager`, dentro de `infrastructure/security/jwt/`.
        5.  Delete os pacotes e classes redundantes.

2.  **Padronizar Nomenclatura de Pacotes**
    -   **Objetivo**: Tornar a estrutura de pacotes mais limpa e intuitiva.
    -   **Passos**:
        1.  Mova o conteúdo de `application/service/impl/` para `application/service/`.
        2.  Delete todos os pacotes `impl`.
        3.  Renomeie `interfaces/api/controller/` para `interfaces/controllers/`.

### Fase 2: Reforçar a Consistência da Arquitetura

1.  **Aplicar Padrão "Ports and Adapters" para Todos os Repositórios**
    -   **Objetivo**: Garantir que toda a comunicação com o banco de dados seja abstraída pelo domínio.
    -   **Passos**:
        -   Para cada interface de repositório em `domain/repository/` (ex: `CompanyRepository`), verifique se existe uma classe `Adapter` correspondente em `infrastructure/persistence/adapter/` (ex: `CompanyRepositoryAdapter`).
        -   Se não existir, crie-a. O `Adapter` deve implementar a interface do domínio e usar o `JpaRepository` correspondente para realizar as operações.

2.  **Padronizar o Uso de Interfaces de Serviço**
    -   **Objetivo**: Definir uma regra clara para a localização das interfaces de serviço.
    -   **Regra Sugerida**:
        -   Se um serviço contém lógica de negócio pura, sua interface pertence ao `domain/service/`.
        -   Se um serviço apenas orquestra casos de uso (chamando repositórios e outros serviços), sua interface (se necessária) pode ficar em `application/usecase/`.
    -   **Passos**:
        -   Revise todos os serviços em `application/service/` e mova as interfaces que representam lógica de negócio central para o domínio.

### Fase 3: Melhorar a Qualidade do Código

1.  **Adotar MapStruct para Mapeamento Automático**
    -   **Objetivo**: Eliminar código de mapeamento manual, repetitivo e propenso a erros.
    -   **Passos**:
        1.  Adicione a dependência do MapStruct ao `pom.xml`.
        2.  Converta os mappers manuais (ex: `UserMapper.java`) em interfaces anotadas com `@Mapper`.
        3.  Exemplo de interface MapStruct:
            ```java
            @Mapper(componentModel = "spring")
            public interface UserMapper {
                User toDomain(UserEntity entity);
                UserEntity toEntity(User domain);
                UserResponse toResponse(User domain);
            }
            ```
        4.  Injete os mappers gerados pelo MapStruct em seus serviços e adaptadores.

### Fase 4: Estratégia de Testes (Prioridade Alta)

1.  **Implementar Testes Unitários (`/src/test`)**
    -   **Objetivo**: Testar a lógica de negócio em isolamento.
    -   **Ferramentas**: JUnit 5, Mockito.
    -   **O que testar**:
        -   **Serviços**: Mockar as dependências (repositórios) e testar cada método público.
        -   **Entidades de Domínio**: Testar métodos que contêm lógica de negócio.
        -   **Validadores e Value Objects**: Garantir que funcionam como esperado.

2.  **Implementar Testes de Integração**
    -   **Objetivo**: Testar a integração entre as camadas.
    -   **Ferramentas**: Spring Boot Test (`@SpringBootTest`, `@DataJpaTest`), MockMvc, Testcontainers.
    -   **O que testar**:
        -   **Repositórios JPA (`@DataJpaTest`)**: Testar se as queries customizadas funcionam contra um banco de dados real (via Testcontainers).
        -   **Controllers (`@SpringBootTest` + `MockMvc`)**: Testar os endpoints da API, validações de entrada, códigos de status HTTP e o corpo das respostas.

3.  **Configurar Cobertura de Testes**
    -   **Objetivo**: Medir a eficácia da suíte de testes.
    -   **Ferramenta**: **JaCoCo**.
    -   **Passos**:
        1.  Configure o plugin `jacoco-maven-plugin` no `pom.xml`.
        2.  Defina uma meta de cobertura (ex: `80%`) e configure o build para falhar se a meta não for atingida.

### Fase 5: Manutenção de Dependências e Análise Estática

1.  **Analisar e Atualizar Dependências**
    -   **Objetivo**: Manter o projeto seguro e atualizado.
    -   **Comandos**:
        -   `mvn versions:display-dependency-updates`: Lista as dependências que podem ser atualizadas.
        -   `mvn dependency:tree`: Analisa dependências transitivas e possíveis conflitos.

2.  **Integrar Análise Estática de Código (SAST)**
    -   **Objetivo**: Encontrar problemas de qualidade e segurança automaticamente.
    -   **Ferramentas**:
        -   **Checkstyle**: Para forçar um padrão de formatação de código.
        -   **SonarLint** (IDE Plugin) / **SonarQube** (Servidor): Para uma análise mais profunda de bugs, vulnerabilidades e "code smells".

## 5. Tabela de Ferramentas Recomendadas

| Ferramenta      | Propósito                                       | Onde Usar                               |
| --------------- | ----------------------------------------------- | --------------------------------------- |
| **MapStruct**   | Geração de código para mapeamento de objetos    | `pom.xml` e substituir mappers manuais  |
| **JUnit 5**     | Framework de testes unitários                   | `pom.xml` (test scope)                  |
| **Mockito**     | Criação de objetos mock para testes unitários   | `pom.xml` (test scope)                  |
| **Testcontainers**| Executar testes de integração com BD real     | `pom.xml` (test scope)                  |
| **JaCoCo**      | Medir a cobertura de código dos testes          | `pom.xml` (build plugin)                |
| **Checkstyle**  | Garantir um estilo de código consistente        | `pom.xml` (build plugin)                |
| **SonarLint**   | Análise de código estática na IDE               | Plugin da sua IDE (IntelliJ, VSCode)    |

---

Este guia deve ser um documento vivo. À medida que o projeto evolui, ele pode e deve ser atualizado. Recomenda-se começar pela **Fase 1** e progredir de forma iterativa.
