# Plano de Migração para Componentes de Segurança

Este documento descreve o plano para migrar os componentes de segurança da estrutura antiga para a nova estrutura baseada em Clean Architecture.

## Fase 1: Implementação Paralela (Concluída)

- ✅ Criar novas classes de segurança na nova estrutura
- ✅ Implementar serviço JWT, filtro de autenticação, e classes de autorização
- ✅ Definir interfaces de domínio para serviços de autenticação e autorização

## Fase 2: Migração de Dados (Em Andamento)

- [x] Criar implementações concretas para os serviços de domínio
- [x] Implementar mapeadores entre entidades antigas e novas
- [x] Configurar repositórios para trabalhar com novas entidades

## Fase 3: Substituição Gradual (Pendente)

- [ ] Atualizar referências nas classes de controladores para usar os novos serviços
- [ ] Substituir injeções de dependência para apontar para as novas implementações
- [ ] Testar exaustivamente a autenticação e autorização com os novos componentes

## Fase 4: Remoção de Código Legado (Pendente)

- [ ] Remover classes antigas após verificar que todas as funcionalidades estão funcionando corretamente
- [ ] Remover importações não utilizadas
- [ ] Limpar configurações obsoletas

## Classes a serem removidas após a migração completa:

1. `com.ocoelhogabriel.manager_user_security.config.SecurityConfig`
2. `com.ocoelhogabriel.manager_user_security.config.JWTAuthFilter`
3. `com.ocoelhogabriel.manager_user_security.handler.PermissaoHandler`
4. `com.ocoelhogabriel.manager_user_security.handler.URLValidator`
5. `com.ocoelhogabriel.manager_user_security.utils.JWTUtil`
6. `com.ocoelhogabriel.manager_user_security.services.AuthServInterface`
7. `com.ocoelhogabriel.manager_user_security.services.impl.AuthServiceImpl`

## Classes a serem modificadas:

1. `com.ocoelhogabriel.manager_user_security.controller.AuthenticationController` - Atualizar para usar os novos serviços
2. `com.ocoelhogabriel.manager_user_security.OpenApiConfig` - Atualizar referências de segurança

## Considerações de Segurança:

- Manter a mesma chave secreta JWT durante a migração para garantir que tokens existentes continuem válidos
- Garantir que as permissões e níveis de acesso sejam preservados na nova implementação
- Realizar testes de segurança para garantir que não haja vulnerabilidades introduzidas