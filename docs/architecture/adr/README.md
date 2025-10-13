# Architecture Decision Records (ADR)

Este diretório contém os registros de decisões de arquitetura para o projeto Manager User Security.

## O que são ADRs?

Um Architecture Decision Record (ADR) é um documento que captura uma decisão importante de arquitetura, junto com seu contexto e consequências. Os ADRs são uma forma de documentar decisões significativas de arquitetura em um formato leve e legível.

## Por que usar ADRs?

Os ADRs são importantes por vários motivos:

- Fornecem contexto para decisões de arquitetura
- Documentam as alternativas consideradas
- Explicam o raciocínio por trás das escolhas
- Ajudam novos membros da equipe a entender o histórico do projeto
- Facilitam a revisão de decisões quando os requisitos mudam

## Formato dos ADRs

Cada ADR é um arquivo Markdown com o seguinte formato:

```markdown
# ADR-NNNN: Título Conciso da Decisão

## Status

[Proposto | Aceito | Depreciado | Substituído por ADR-XXXX]

## Contexto

Descrição do problema ou situação que motivou essa decisão arquitetural.
Explique as forças em jogo, incluindo tecnológicas, comerciais, sociais e
específicas do projeto.

## Decisão

Descrição clara e concisa da decisão tomada.

## Alternativas Consideradas

Quais outras alternativas foram consideradas? Por que elas não foram escolhidas?

## Consequências

Quais são as consequências positivas e negativas dessa decisão?
Como ela afeta outros componentes do sistema?
Quais são os trade-offs?

## Referências

Links para documentos externos, padrões de design, exemplos ou outras fontes relevantes.
```

## Lista de ADRs

Aqui estão os ADRs atuais do projeto:

- [ADR-0001: Adoção da Clean Architecture](adr-0001-clean-architecture.md)
- [ADR-0002: Autenticação Baseada em JWT](adr-0002-jwt-authentication.md)
- [ADR-0003: Padrões de Organização de Imports](adr-0003-import-organization.md)

## Como Criar um Novo ADR

1. Copie o template em `_template.md`
2. Nomeie o novo arquivo como `adr-NNNN-titulo-descritivo.md`, onde NNNN é o próximo número sequencial
3. Preencha o conteúdo seguindo o formato acima
4. Adicione um link para o novo ADR neste arquivo README.md
5. Submeta para revisão

## Referências

- [Architectural Decision Records](https://adr.github.io/)
- [Documenting Architecture Decisions by Michael Nygard](https://cognitect.com/blog/2011/11/15/documenting-architecture-decisions)
