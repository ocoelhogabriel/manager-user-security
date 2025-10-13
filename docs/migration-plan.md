# Plano de Migração - Manager User Security

Este documento apresenta o plano de migração da implementação antiga (`manager_user_security`) para a nova implementação com Clean Architecture (`usersecurity`).

## Estado Atual da Migração

A migração está em andamento, com a seguinte distribuição de progresso por componentes:

- ✅ **Framework de Segurança**: Os componentes principais de segurança e autenticação foram implementados na nova estrutura.
- ✅ **Entidades de Domínio Core**: As entidades principais (User, Role, Permission, Resource) foram implementadas.
- ✅ **Persistência Core**: Entidades JPA, repositórios e mapeadores para componentes principais foram implementados.
- ✅ **Serviços Core**: Os serviços principais de autenticação, autorização e gerenciamento de permissões foram implementados.
- ❌ **Interfaces da API**: Controladores REST e DTOs específicos ainda precisam ser implementados.
- ❌ **Entidades Específicas de Negócio**: Entidades específicas como Empresa, Planta, Silo, etc. ainda precisam ser migradas.
- ❌ **Implementação de Casos de Uso Específicos**: Casos de uso específicos de negócio ainda precisam ser implementados.

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
- ⏳ Implementação das entidades de domínio específicas
- ⏳ Implementação dos casos de uso específicos
- ⏳ Implementação das interfaces da API

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

1. **Implementar controladores REST principais**:
   - AuthenticationController
   - UserController
   - RoleController
   - ResourceController

2. **Criar DTOs para interfaces da API**:
   - DTOs para autenticação
   - DTOs para gerenciamento de usuários
   - DTOs para gerenciamento de perfis
   - DTOs para gerenciamento de recursos

3. **Iniciar a implementação das entidades de domínio específicas**:
   - Empresa/Company
   - Planta/Plant
   - Silo e TipoSilo
   - SiloModulo

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

## Conclusão

A migração está em bom progresso, com os componentes core já implementados. O foco agora deve ser na implementação dos componentes específicos de negócio e interfaces da API, seguindo a estrutura e padrões estabelecidos para a nova arquitetura.