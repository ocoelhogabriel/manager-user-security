# Guia de Padrões de Codificação

Este documento descreve os padrões de codificação adotados no projeto Manager User Security.

## Organização de Imports

### Ordem de Imports

Todos os imports devem seguir a seguinte ordem, separados por uma linha em branco entre cada grupo:

1. Imports estáticos (em ordem alfabética)
2. Imports do Java (java.*) (em ordem alfabética)
3. Imports de terceiros (em ordem alfabética)
4. Imports do projeto (com.ocoelhogabriel.*) (em ordem alfabética)

Exemplo:

```java
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import com.ocoelhogabriel.manager_user_security.domain.entities.UsuarioModel;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dto.UsuarioDTO;
```

### Imports a Evitar

- Evite usar `*` em importações para manter a clareza sobre quais classes estão sendo usadas
- Evite importar classes não utilizadas

### Ferramentas de Verificação

O projeto utiliza o plugin `impsort-maven-plugin` para garantir a consistência dos imports. Execute o script `format-code` para formatar automaticamente os imports.

## Convenções de Nomenclatura

### Classes

- Utilize nomes em PascalCase (primeira letra maiúscula)
- Use substantivos ou frases nominais
- Sufixos comuns:
  - `Controller`: Para controladores REST
  - `Service`: Para serviços
  - `Repository`: Para repositórios
  - `Model`: Para entidades de domínio
  - `DTO`: Para objetos de transferência de dados
  - `Exception`: Para exceções personalizadas

### Métodos

- Utilize nomes em camelCase (primeira letra minúscula)
- Inicie com verbos (ex: get, create, update, delete)
- Nomes descritivos que indiquem a função

### Variáveis

- Utilize nomes em camelCase
- Nomes descritivos, evite abreviações obscuras
- Constantes em SNAKE_CASE_MAIÚSCULO

### Pacotes

- Nomes em minúsculas, separados por pontos
- Estrutura hierárquica baseada na arquitetura do sistema

## Formatação de Código

### Indentação

- 4 espaços para indentação (sem tabs)
- Chaves em nova linha para métodos e classes
- Chaves na mesma linha para estruturas de controle (if, for, etc.)

### Comprimento de Linha

- Limite de 120 caracteres por linha
- Quebras de linha após vírgulas e antes de operadores

### Espaçamento

- Um espaço após palavras-chave (if, for, while, etc.)
- Um espaço antes e depois de operadores (=, +, -, etc.)
- Sem espaço entre nome de método e parênteses

## Boas Práticas

### Comentários

- Comentários Javadoc para classes e métodos públicos
- Comentários explicativos para lógicas complexas
- Evite comentários óbvios ou redundantes

### Tratamento de Exceções

- Use exceções específicas, evite `Exception` genérica
- Documente exceções lançadas em Javadoc
- Registre informações de contexto em exceções (logs)

### Logging

- Use o logger do SLF4J
- Níveis apropriados: ERROR, WARN, INFO, DEBUG, TRACE
- Mensagens descritivas com contexto relevante

### Testes

- Um assert por teste (quando possível)
- Nomenclatura descritiva: `should_DoSomething_When_SomethingHappens`
- Separe testes por categorias (unidade, integração, etc.)

## Verificação de Conformidade

O projeto utiliza as seguintes ferramentas para garantir a conformidade com os padrões:

1. **Checkstyle**: Verifica estilo e formatação
2. **PMD**: Analisa código em busca de problemas potenciais
3. **SpotBugs**: Detecta bugs potenciais
4. **EditorConfig**: Mantém consistência entre IDEs

Execute o script `verify-code` para verificar se o código está em conformidade com os padrões definidos.

## Integração com IDE

### IntelliJ IDEA

1. Importe o arquivo de estilo em `config/intellij-code-style.xml`
2. Configure o plugin Save Actions para formatar o código automaticamente

### Eclipse

1. Importe o arquivo de estilo em `config/eclipse-code-style.xml`
2. Configure o Clean-Up para ser aplicado ao salvar

### VS Code

1. Instale as extensões recomendadas no arquivo `.vscode/extensions.json`
2. As configurações já estão definidas em `.vscode/settings.json`

## Processo de Revisão

Durante as revisões de código, os seguintes aspectos serão avaliados:

1. Conformidade com padrões de código
2. Organização adequada de imports
3. Cobertura de testes
4. Qualidade geral do código

---

Última atualização: 12 de outubro de 2025
