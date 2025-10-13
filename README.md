# Manager User Security - Documentação de Migração

Este repositório contém o código do projeto Manager User Security, que está passando por uma migração de arquitetura para implementar os princípios de Clean Architecture.

## Estrutura do Projeto

Atualmente, o projeto possui duas implementações paralelas:

1. **Implementação Original** (`com.ocoelhogabriel.manager_user_security`): A implementação original do sistema.
2. **Nova Implementação** (`com.ocoelhogabriel.usersecurity`): A nova implementação seguindo os princípios de Clean Architecture.

A migração está sendo realizada progressivamente, com novos componentes sendo criados na nova estrutura enquanto mantemos a implementação original funcionando.

## Documentos Importantes

### Documentação de Planejamento

- [Mapeamento do Projeto](temp/project-mapping.md) - Mapeamento detalhado entre a estrutura antiga e nova
- [Plano de Migração](docs/migration-plan.md) - Plano para migrar completamente para a nova arquitetura
- [Benefícios da Clean Architecture](docs/clean-architecture-benefits.md) - Explicação das vantagens da nova arquitetura

### Documentação Técnica

- [Instruções para Copilot](docs/copilot-instructions.md) - Contexto do projeto para o GitHub Copilot
- [Configuração de Migração](docs/configuration-migration.md) - Guia para migrar configurações
- [Visão Geral da Arquitetura](docs/architecture-overview.md) - Detalhes da arquitetura do sistema
- [Configuração do Ambiente](docs/development-setup.md) - Instruções para configurar o ambiente de desenvolvimento
- [Mapeamento de Classes](docs/class-mapping.md) - Mapeamento detalhado das classes entre as arquiteturas

## Status da Migração

A migração está atualmente em andamento, com o seguinte progresso:

- ✅ **Estrutura de Clean Architecture**: A estrutura básica foi estabelecida
- ✅ **Componentes Core**: Implementações core (User, Role, Permission, Resource) concluídas
- ✅ **Infraestrutura de Segurança**: Componentes de segurança e autenticação implementados
- ❌ **Interfaces da API**: Controladores e DTOs da API ainda precisam ser implementados
- ❌ **Componentes Específicos de Negócio**: Entidades e serviços específicos pendentes
- ❌ **Testes**: Implementação de testes unitários e de integração pendentes

## Nova Estrutura de Clean Architecture

A nova implementação segue a estrutura de Clean Architecture com as seguintes camadas:

### 1. Camada de Domínio (`domain`)
- **Entidades**: Objetos de negócio com regras e comportamentos
- **Repositórios**: Interfaces para persistência de dados
- **Serviços**: Interfaces para serviços de domínio
- **Exceções**: Exceções específicas de domínio

### 2. Camada de Aplicação (`application`)
- **Serviços**: Implementações dos casos de uso
- **DTOs**: Objetos de transferência de dados internos
- **Exceções**: Exceções específicas da aplicação

### 3. Camada de Infraestrutura (`infrastructure`)
- **Persistência**: Implementações de repositórios, entidades JPA, configurações
- **Segurança**: Componentes de autenticação e autorização
- **Configuração**: Configurações técnicas

### 4. Camada de Interfaces (`interfaces`)
- **API REST**: Controladores, DTOs, mapeadores
- **Tratamento de Exceções**: Manipuladores globais de exceções

## Como Contribuir para a Migração

1. **Entenda o mapeamento**: Consulte o [Mapeamento do Projeto](temp/project-mapping.md) para entender a relação entre classes antigas e novas.
2. **Siga o plano**: Use o [Plano de Migração](docs/migration-plan.md) como guia para implementar novos componentes.
3. **Mantenha a consistência**: Siga os padrões de nomenclatura e estrutura já estabelecidos na nova arquitetura.
4. **Atualize a documentação**: Mantenha o mapeamento e outros documentos atualizados à medida que implementa novos componentes.
5. **Teste adequadamente**: Implemente testes unitários para os componentes que desenvolver.

## Executando o Projeto

### Configuração Atual (Implementação Original)

```bash
# Compile o projeto
./mvnw clean install

# Execute o projeto
java -jar target/siloapi.war
```

### Nova Configuração (Migração em Andamento)

```bash
# Compile o projeto
./mvnw clean install

# Execute com o perfil de configuração clean
java -jar target/siloapi.war --spring.profiles.active=clean
```

## Contato e Suporte

Para questões relacionadas à migração ou ao projeto em geral, entre em contato com a equipe de desenvolvimento.

---

**Nota**: Este projeto está em processo ativo de migração. Alguns componentes podem não funcionar completamente na nova arquitetura até que a migração seja concluída.