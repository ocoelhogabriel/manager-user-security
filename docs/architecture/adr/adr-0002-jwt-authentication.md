# ADR-0002: Autenticação Baseada em JWT

## Status

Aceito

## Contexto

O sistema Manager User Security requer um mecanismo de autenticação robusto e seguro. Como estamos desenvolvendo uma aplicação distribuída, precisamos de um método de autenticação que:

- Seja stateless para facilitar a escalabilidade horizontal
- Permita autenticação entre diferentes serviços e clientes
- Suporte autorização baseada em roles/permissões
- Minimize o tráfego de rede para validação de credenciais
- Forneça expiração automática de sessões
- Seja compatível com padrões de segurança modernos

A solução deve ser segura contra ataques comuns e oferecer uma experiência de usuário satisfatória em termos de desempenho e facilidade de uso.

## Decisão

Implementar autenticação baseada em JWT (JSON Web Token) com as seguintes características:

1. **Estrutura do Token**:
   - Header: Algoritmo de assinatura (HS256)
   - Payload: Identificador do usuário, roles, permissões, timestamp de emissão, timestamp de expiração
   - Assinatura: HMAC SHA-256 com chave secreta

2. **Ciclo de Vida**:
   - Token de acesso com vida curta (15 minutos)
   - Token de refresh com vida mais longa (7 dias)
   - Mecanismo de blacklist para revogação imediata quando necessário

3. **Implementação Técnica**:
   - Usar biblioteca Auth0 JWT para manipulação de tokens
   - Implementar filtros personalizados no Spring Security para processamento de JWT
   - Armazenar tokens de refresh em banco de dados para controle de revogação
   - Validação de token em cada requisição via filtro de segurança

4. **Segurança**:
   - Tokens assinados com chave secreta robusta
   - Armazenar chaves em variáveis de ambiente/cofre seguro
   - Rotação periódica de chaves de assinatura
   - Validação de claims para prevenir manipulação

## Alternativas Consideradas

### Autenticação Baseada em Sessão

**Prós:**

- Familiar e fácil de implementar
- Revogação simples de sessões

**Contras:**

- Requer armazenamento de estado no servidor
- Problemas com escalabilidade horizontal
- Desafios com múltiplos serviços/domínios

### OAuth 2.0 Completo

**Prós:**

- Padrão de indústria maduro
- Suporte a múltiplos provedores de identidade
- Delegação de autorização

**Contras:**

- Complexidade significativa para implementação completa
- Overhead desnecessário para requisitos atuais
- Curva de aprendizado mais íngreme

### API Keys Estáticas

**Prós:**

- Simplicidade extrema
- Baixo overhead computacional

**Contras:**

- Gerenciamento de chaves complexo
- Sem suporte nativo para expiração
- Segurança limitada

## Consequências

### Positivas

- **Escalabilidade**: Sistema totalmente stateless, facilitando escalabilidade horizontal
- **Performance**: Validação rápida sem necessidade de consultas ao banco a cada requisição
- **Segurança**: Tokens assinados garantem autenticidade
- **Flexibilidade**: Compatível com diversas arquiteturas (SPA, mobile apps, microserviços)
- **Informação Rica**: Payload pode conter dados relevantes como roles e permissões

### Negativas

- **Tamanho do Token**: Tokens JWT podem ser relativamente grandes, aumentando o tamanho das requisições
- **Revogação**: Revogação imediata requer implementação adicional (blacklist)
- **Complexidade**: Implementação de refresh tokens e rotação de chaves adiciona complexidade
- **Exposição de Informações**: Cuidado necessário para não incluir dados sensíveis no payload
- **Segurança da Chave**: A segurança do sistema depende da proteção adequada da chave de assinatura

## Referências

- [Introduction to JSON Web Tokens](https://jwt.io/introduction/)
- [RFC 7519: JSON Web Token (JWT)](https://tools.ietf.org/html/rfc7519)
- [Spring Security com JWT](https://spring.io/guides/tutorials/spring-boot-oauth2/)
- [JWT Best Practices](https://auth0.com/blog/a-look-at-the-latest-draft-for-jwt-bcp/)
