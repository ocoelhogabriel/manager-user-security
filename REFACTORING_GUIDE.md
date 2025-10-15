# Guia de Refatoração e Boas Práticas - manager-user-security

## 1. Visão Geral

O projeto `manager-user-security` possui uma base arquitetural sólida, utilizando uma abordagem em camadas que se inspira em princípios do Domain-Driven Design (DDD) e da Arquitetura Hexagonal (Ports and Adapters). O código é separado em camadas de domínio, aplicação, infraestrutura e interfaces, o que é uma excelente prática.

Este documento serve como um guia de referência para:
1.  **Mapear** a estrutura atual do projeto.
2.  **Identificar** inconsistências e pontos de melhoria.
3.  **Definir** um plano de ação claro para refatorar o código, aumentar a cobertura de testes e melhorar a manutenibilidade geral.

---

## 2. Status Atual e Próximos Passos (Atualizado)

Após uma análise detalhada, várias melhorias do plano original já foram implementadas. Esta seção resume o status atual e define as próximas prioridades.

### Concluído

*   **Fase 1: Limpeza e Padronização Estrutural**
    *   ✅ **Consolidação da Configuração de Segurança**: A lógica de segurança e JWT foi unificada em `infrastructure/security`.
    *   ✅ **Padronização de Pacotes de Serviço**: Os pacotes `impl` foram removidos.
    *   ✅ **Padronização dos Controllers**: Os controllers foram movidos para o pacote `interfaces/controllers`.

*   **Fase 2: Consistência da Arquitetura**
    *   ✅ **Padrão "Ports and Adapters"**: Todos os repositórios do domínio possuem uma implementação `Adapter` na camada de infraestrutura.
    *   ✅ **Uso de Interfaces de Serviço**: Os serviços principais, como `AuthenticationService`, já utilizam a interface do domínio.

*   **Fase 3: Qualidade do Código**
    *   ✅ **MapStruct Configurado**: A dependência e o plugin do MapStruct já estão no `pom.xml`, prontos para uso.
    *   ✅ **Mapeadores Consolidados**: Todos os mappers manuais foram substituídos por interfaces MapStruct consolidadas, e a lógica de negócio foi movida para os serviços.

*   **Fase 4 (Parcial): Estratégia de Testes**
    *   ✅ **JaCoCo Configurado**: O plugin para medição de cobertura de testes está presente no `pom.xml`.
    *   ✅ **Testcontainers Adicionado**: As dependências do Testcontainers foram incluídas no `pom.xml`.

*   **Fase 5: Manutenção de Dependências e Análise Estática**
    *   ✅ **Integrar Análise Estática de Código**: O plugin `checkstyle-maven-plugin` foi adicionado e configurado no `pom.xml`, e um arquivo `checkstyle.xml` básico foi criado.

### Pendências e Plano de Ação

A seguir estão as tarefas prioritárias para finalizar a refatoração e fortalecer o projeto.

#### **1. Implementar Funcionalidade de Logout (Prioridade Alta)**
-   ✅ **Objetivo**: Garantir que os tokens JWT possam ser invalidados, permitindo um logout seguro.
-   ✅ **Ação**: A lógica de invalidação de token foi implementada no método `AuthenticationService.invalidateToken(String token)` usando uma blacklist em memória.

#### **2. Criar Controllers Faltantes**
-   ✅ **Objetivo**: Expor as operações das entidades `Plant` e `Logger` através de endpoints REST.
-   ✅ **Ação**: `PlantController.java` e `LoggerController.java` foram criados no pacote `interfaces/controllers`.

#### **3. Fortalecer a Estratégia de Testes**
-   **Objetivo**: Aumentar a confiança nas regras de negócio e na integração com o banco de dados.
-   **Ações**:
    1.  **Escrever Testes de Integração**: Crie testes para os repositórios JPA, especialmente para queries customizadas.
    2.  **Aumentar Cobertura de Testes Unitários**: Foque nos serviços do domínio e da aplicação, mockando dependências para testar a lógica de negócio em isolamento.
    3.  **Gerar Relatório de Cobertura**: Execute `mvn clean install` para que o JaCoCo gere o relatório e analise os resultados para identificar áreas críticas sem testes.

---

## 3. Mapeamento da Arquitetura Original (Referência)

A estrutura do projeto está dividida nas seguintes camadas lógicas:

#### `src/main/java/com/ocoelhogabriel/manager_user_security`

-   **Camada de Domínio (`domain`)**: O coração da aplicação.
-   **Camada de Aplicação (`application`)**: Orquestra o domínio para executar casos de uso.
-   **Camada de Infraestrutura (`infrastructure`)**: Implementa as interfaces, lidando com preocupações externas.
-   **Camada de Interfaces (`interfaces`)**: Expõe a API REST.

## 4. Princípios Orientadores da Refatoração

-   **Consistência**: Um padrão, um lugar.
-   **Testabilidade**: Código não testado é código legado.
-   **Clareza > Inteligência**: Escreva código fácil de entender.
-   **Separação de Responsabilidades (SoC)**: Reforce a separação entre as camadas.

## 5. Tabela de Ferramentas Recomendadas

| Ferramenta      | Propósito                                       | Onde Usar                               |
| --------------- | ----------------------------------------------- | --------------------------------------- |
| **MapStruct**   | Geração de código para mapeamento de objetos    | `pom.xml` e substituir mappers manuais  |
| **JUnit 5**     | Framework de testes unitários                   | `pom.xml` (test scope)                  |
| **Mockito**     | Criação de objetos mock para testes unitários   | `pom.xml` (test scope)                  |
| **Testcontainers**| ✅ Executar testes de integração com BD real     | `pom.xml` (test scope)                  |
| **JaCoCo**      | Medir a cobertura de código dos testes          | `pom.xml` (build plugin)                |
| **Checkstyle**  | ✅ Garantir um estilo de código consistente        | `pom.xml` (build plugin)                |
| **SonarLint**   | Análise de código estática na IDE               | Plugin da sua IDE (IntelliJ, VSCode)    |
