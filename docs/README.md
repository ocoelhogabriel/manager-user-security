# Documentação do Manager User Security

Este diretório contém a documentação completa do projeto Manager User Security. Utilize este índice para navegar pelos diferentes documentos disponíveis.

## Documentos Principais

### Visão Geral e Guias

- [Resumo das Melhorias Implementadas](improvements-summary.md) - Resumo de todas as melhorias realizadas no projeto
- [Manual do Desenvolvedor](developer-manual.md) - Guia completo para desenvolvedores que trabalham no projeto

### Arquitetura

- [Visão Geral da Arquitetura](architecture/architecture-overview.md) - Descrição detalhada da arquitetura do sistema
- [Diagramas de Arquitetura](architecture/architecture-diagrams.md) - Representações visuais da arquitetura do sistema
- [Melhores Práticas para Clean Architecture](architecture/clean-architecture-best-practices.md) - Guia de melhores práticas para trabalhar com Clean Architecture
- [Decisões de Arquitetura (ADRs)](architecture/adr/README.md) - Registro de decisões arquiteturais importantes:
  - [ADR-0001: Adoção da Clean Architecture](architecture/adr/adr-0001-clean-architecture.md)
  - [ADR-0002: Autenticação Baseada em JWT](architecture/adr/adr-0002-jwt-authentication.md)
  - [ADR-0003: Padrões de Organização de Imports](architecture/adr/adr-0003-import-organization.md)

### Padrões e Convenções

- [Guia de Padrões de Codificação](coding-standards/coding-standards.md) - Padrões de código adotados no projeto

### Segurança

- [Guia de Segurança](security/security-guide.md) - Melhores práticas e configurações de segurança

### Operações

- [Guia de Deployment](operations/deployment-guide.md) - Instruções detalhadas para implantação em diferentes ambientes
- [Guia de Monitoramento](operations/monitoring-guide.md) - Práticas recomendadas para monitoramento e observabilidade

## Documentação de API

A documentação da API está disponível via Swagger/OpenAPI:

- Desenvolvimento local: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- Ambiente de teste: [https://api-test.example.com/swagger-ui.html](https://api-test.example.com/swagger-ui.html)
- Ambiente de produção: [https://api.example.com/swagger-ui.html](https://api.example.com/swagger-ui.html)

## Guias de Contribuição

- [Guia de Contribuição](../CONTRIBUTING.md) - Como contribuir para o projeto
- [Código de Conduta](../CODE_OF_CONDUCT.md) - Diretrizes de comportamento para contribuidores

## Estrutura da Documentação

```plaintext
docs/
├── architecture/
│   ├── adr/                      # Architecture Decision Records
│   │   ├── README.md
│   │   ├── _template.md
│   │   ├── adr-0001-clean-architecture.md
│   │   ├── adr-0002-jwt-authentication.md
│   │   └── adr-0003-import-organization.md
│   ├── architecture-overview.md  # Visão geral da arquitetura
│   ├── architecture-diagrams.md  # Diagramas da arquitetura
│   └── clean-architecture-best-practices.md  # Melhores práticas
├── coding-standards/
│   └── coding-standards.md       # Padrões de codificação
├── operations/
│   ├── deployment-guide.md       # Guia de deployment
│   └── monitoring-guide.md       # Guia de monitoramento
├── security/
│   └── security-guide.md         # Guia de segurança
├── developer-manual.md           # Manual do desenvolvedor
├── improvements-summary.md       # Resumo das melhorias implementadas
└── README.md                     # Este arquivo
```

## Manutenção da Documentação

Esta documentação deve ser mantida atualizada sempre que houver mudanças significativas no projeto. Contribuições para melhorar a documentação são sempre bem-vindas.

Última atualização: 12 de outubro de 2025
