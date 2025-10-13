# Plano de Migração - Manager User Security

Este documento apresenta o plano de migração da implementação antiga (`manager_user_security`) para a nova implementação com Clean Architecture (`usersecurity`).

## Estado Atual da Migração

A migração está em andamento, com a seguinte distribuição de progresso por componentes:

- ✅ **Framework de Segurança**: Os componentes principais de segurança e autenticação foram implementados na nova estrutura.
- ✅ **Entidades de Domínio Core**: As entidades principais (User, Role, Permission, Resource) foram implementadas.
- ✅ **Entidades Específicas de Negócio**: As entidades específicas (Company, Plant, Logger) foram implementadas.
- ✅ **Persistência Core**: Entidades JPA, repositórios e mapeadores para componentes principais e específicos foram implementados.
- ✅ **Serviços Core**: Os serviços principais de autenticação, autorização e gerenciamento de permissões foram implementados.
- ✅ **Repositórios**: Interfaces de repositório e implementações JPA para User, Role, Permission, Resource, Company, Plant e Logger foram implementadas.
- ✅ **Interfaces da API**: Controladores REST para autenticação e gerenciamento de recursos foram implementados (AuthenticationController, UserController, RoleController, ResourceController, CompanyController, PlantController, LoggerController).
- ✅ **DTOs de API**: DTOs para as interfaces da API foram implementados (Authentication, User, Role, Resource, Company, Plant, Logger).
- ✅ **Implementação de Casos de Uso**: Casos de uso para componentes principais foram implementados (AuthenticationUseCase, CompanyUseCase, PlantUseCase, LoggerUseCase).

## Estratégia de Migração

### 1. Abordagem Gradual (Em Andamento)

Estamos seguindo uma abordagem gradual, onde:

1. A nova implementação está sendo construída paralelamente à existente
2. Os componentes core foram implementados primeiro
3. Os componentes específicos de negócio serão implementados progressivamente
4. A transição será feita de forma gradual para minimizar riscos

### 2. Fases de Migração

#### Fase 1: Infraestrutura Core (Concluída)
- ✅ Definição da estrutura de pacotes seguindo Clean Architecture
- ✅ Implementação das entidades de domínio principais
- ✅ Implementação dos serviços de segurança e autenticação
- ✅ Implementação do framework de exceções

#### Fase 2: Componentes Específicos de Negócio (Em Andamento)
- ✅ Implementação das entidades de domínio específicas (Company, Plant, Logger)
- ✅ Implementação dos repositórios específicos de negócio
- ⏳ Implementação dos casos de uso específicos
- ⏳ Implementação das interfaces da API completas

#### Fase 3: Transição e Integração (Pendente)
- ❌ Testes de integração completos
- ❌ Migração de dados entre estruturas antigas e novas
- ❌ Configuração das propriedades de aplicação
- ❌ Integração com interfaces de usuário existentes

#### Fase 4: Finalização da Migração (Pendente)
- ❌ Remoção do código antigo
- ❌ Consolidação da documentação
- ❌ Verificação final de qualidade e segurança

## Tarefas Imediatas Recomendadas

> **Nota**: As tarefas relacionadas a Silo, SiloType, Medicao, Firmware e Pendencias foram removidas do escopo do projeto conforme decisão da equipe.

1. **Implementar testes unitários e de integração**:
   - Testes para os controladores REST
   - Testes para casos de uso
   - Testes para serviços de aplicação
   - Testes para repositórios

2. **Refinar a documentação da API**:
   - Melhorar anotações OpenAPI/Swagger
   - Documentar exemplos de requisições e respostas
   - Atualizar documentação de endpoints

3. **Desenvolver estratégia de migração de dados**:
   - Criar scripts de migração para dados existentes
   - Implementar procedimento de backup/rollback

4. **Configurar monitoramento e logging**:
   - Implementar logging estruturado
   - Configurar monitoramento de performance
   - Configurar alertas para erros críticos

## Desafios e Considerações

### 1. Compatibilidade com Código Existente

- Garantir que os endpoints da API mantenham a mesma interface externa
- Manter a compatibilidade com tokens JWT existentes
- Garantir que as validações de permissão funcionem da mesma forma

### 2. Migração de Dados

- Desenvolver estratégia para migração de dados entre estruturas
- Considerar migração gradual ou por lote
- Garantir integridade dos dados durante a migração

### 3. Desempenho e Escalabilidade

- Otimizar consultas ao banco de dados com a nova estrutura
- Garantir que a nova implementação mantenha ou melhore o desempenho
- Implementar caching onde apropriado

### 4. Testes

- Implementar testes unitários para componentes principais
- Implementar testes de integração para fluxos críticos
- Realizar testes de carga para garantir escalabilidade

## Monitoramento de Progresso

O progresso da migração será monitorado através do arquivo `project-mapping.md`, que será atualizado regularmente para refletir o estado atual da migração.

## Decisões de Escopo

### Componentes Removidos do Escopo

Após análise de requisitos e discussão com a equipe, os seguintes componentes foram removidos do escopo do projeto:

- **Silo**: Entidades, repositórios, serviços e controladores relacionados a silos
- **SiloType (TipoSilo)**: Classificação e tipos de silos
- **SiloModule (ModuloSilo)**: Módulos de silos e sua gestão
- **Medicao (Measurement)**: Medições relacionadas a silos
- **Pendencia (Pendency)**: Pendências no sistema
- **Firmware**: Gerenciamento de firmwares

Esta decisão foi tomada para focar o desenvolvimento nos componentes core de segurança e gerenciamento de usuários, empresas, plantas e logs, simplificando a migração e reduzindo o escopo inicial do projeto.

## Conclusão

A migração está em fase avançada, com todos os componentes core e a maioria dos componentes específicos de negócio já implementados. O foco atual está em finalizar os testes, refinar a documentação, e preparar a estratégia de migração de dados para uma transição completa para a nova arquitetura.

A arquitetura Clean Architecture adotada tem demonstrado benefícios em termos de organização do código, separação de responsabilidades e facilidade de teste. O projeto está bem estruturado e pronto para as fases finais de desenvolvimento e implantação.