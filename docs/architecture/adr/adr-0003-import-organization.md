# ADR-0003: Padrões de Organização de Imports

## Status

Aceito

## Contexto

À medida que o projeto Manager User Security cresce, a organização consistente do código torna-se cada vez mais importante para a legibilidade e manutenção. Um aspecto específico que requer padronização é a organização das declarações de import nas classes Java.

Atualmente, observamos inconsistências na organização de imports entre diferentes arquivos do projeto, incluindo:

- Ordem inconsistente de imports
- Mistura de imports estáticos e não-estáticos sem separação clara
- Uso inconsistente de importações curinga (*)
- Falta de agrupamento lógico entre imports Java, imports de terceiros e imports do projeto

Essas inconsistências dificultam a leitura do código, complicam revisões de código e podem levar a conflitos em mesclagens de código.

## Decisão

Adotar um padrão claro e consistente para organização de imports em todos os arquivos Java do projeto, com as seguintes regras:

1. **Ordem de Agrupamento**:
   - Primeiro grupo: Imports estáticos em ordem alfabética
   - Segundo grupo: Imports Java (java.*) em ordem alfabética
   - Terceiro grupo: Imports de terceiros (não-java, não do projeto) em ordem alfabética
   - Quarto grupo: Imports do próprio projeto (com.ocoelhogabriel.*) em ordem alfabética

2. **Separação de Grupos**:
   - Linha em branco obrigatória entre cada grupo de imports
   - Sem linhas em branco dentro de cada grupo

3. **Regras Específicas**:
   - Nunca usar imports curinga (*), sempre importar classes específicas
   - Todos os imports devem estar no cabeçalho da classe (sem imports dentro de blocos de código)
   - Remover imports não utilizados
   - Sem imports redundantes (ex: java.lang.*)

4. **Implementação Técnica**:
   - Configurar o plugin `impsort-maven-plugin` para ordenar automaticamente os imports
   - Adicionar verificação Checkstyle para validar a conformidade
   - Criar script de formatação de código para aplicar as regras automaticamente
   - Configurar IDEs comuns (Eclipse, IntelliJ) com as mesmas regras

## Alternativas Consideradas

### Sem Padronização Formal

**Prós:**
- Menos sobrecarga inicial para desenvolvedores
- Sem necessidade de configurar ferramentas adicionais

**Contras:**
- Inconsistência crescente ao longo do tempo
- Dificuldade em revisões de código
- Conflitos de mesclagem mais frequentes

### Apenas Ordem Alfabética Simples

**Prós:**
- Simplicidade na regra
- Fácil de lembrar sem ferramentas

**Contras:**
- Não agrupa logicamente os imports por origem
- Mistura imports de sistema com imports do projeto

### Ferramenta IDE-Específica

**Prós:**
- Integração perfeita com IDE específica
- Automação imediata para usuários dessa IDE

**Contras:**
- Não portável entre diferentes IDEs
- Difícil de padronizar em equipes com diferentes preferências de IDE

## Consequências

### Positivas

- **Consistência**: Todos os arquivos seguirão o mesmo padrão de organização de imports
- **Legibilidade**: Agrupamento lógico facilita o entendimento das dependências
- **Automação**: Formatação automática reduz o esforço manual
- **Redução de Conflitos**: Menos conflitos em mesclagens relacionados a imports
- **Identificação de Dependências**: Facilidade para identificar dependências externas vs. internas

### Negativas

- **Esforço Inicial**: Tempo necessário para configurar ferramentas e adaptar código existente
- **Curva de Aprendizado**: Desenvolvedores precisam se adaptar às novas regras
- **Manutenção de Configuração**: Necessidade de manter configurações de formatação atualizadas
- **Possível Resistência**: Desenvolvedores podem resistir a mudanças em seus hábitos de codificação

## Referências

- [Google Java Style Guide - Imports](https://google.github.io/styleguide/javaguide.html#s3.3-import-statements)
- [Oracle Code Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-imports.html)
- [Maven ImportSort Plugin](https://code.revelc.net/impsort-maven-plugin/)
- [Checkstyle Import Control](https://checkstyle.sourceforge.io/config_imports.html)
