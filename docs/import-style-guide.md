# Guia de Padronização de Importações

Este documento define as regras para organização e estilo de importações no projeto Manager User Security.

## Ordem das Importações

As importações devem seguir esta ordem:

1. Importações estáticas (se houver)
2. Linha em branco
3. Pacotes `java.*`
4. Linha em branco
5. Pacotes `javax.*` e `jakarta.*`
6. Linha em branco
7. Pacotes `org.*` (incluindo Spring)
8. Linha em branco
9. Pacotes `com.*` (incluindo o projeto)
10. Linha em branco
11. Outros pacotes (`io.*`, etc.)

## Exemplos

### Correto

```java
package com.ocoelhogabriel.manager_user_security.example;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.OK;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

import com.ocoelhogabriel.manager_user_security.domain.entities.User;
import com.ocoelhogabriel.manager_user_security.domain.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
```

### Incorreto

```java
package com.ocoelhogabriel.manager_user_security.example;

import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.springframework.http.HttpStatus.OK;
import com.ocoelhogabriel.manager_user_security.domain.entities.User;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
```

## Regras Adicionais

1. **Sem importações com asterisco**: Não use `import java.util.*`. Importe classes específicas.
2. **Importações não utilizadas**: Remova todas as importações não utilizadas.
3. **Importações redundantes**: Evite importar classes do mesmo pacote.
4. **Formatação automática**: Use os scripts `format-code.cmd` ou `format-code.sh` antes de fazer commit.

## Ferramentas Configuradas

O projeto está configurado com:

1. **EditorConfig**: Arquivo `.editorconfig` para consistência entre editores
2. **Maven Formatter Plugin**: Para formatação automática do código
3. **Impsort Maven Plugin**: Para organizar importações
4. **Checkstyle**: Para verificar conformidade com as regras de estilo

## Como Usar

### Verificar Estilo de Código

```bash
mvn checkstyle:check
```

### Formatar Código e Organizar Importações

No Windows:
```
format-code.cmd
```

No Linux/MacOS:
```
./format-code.sh
```

### Verificar e Corrigir Todos os Problemas

No Windows:
```
verify-code.cmd
```

## Configuração IDE

### IntelliJ IDEA

1. Instale o plugin EditorConfig
2. Importe o estilo de código do projeto
3. Configure o estilo de importação:
   - Settings > Editor > Code Style > Java > Imports
   - Configure para seguir o padrão definido acima

### VS Code

1. Instale a extensão EditorConfig for VS Code
2. Instale a extensão Java Extension Pack
3. Configure o estilo de importação:
   - settings.json:
   ```json
   "java.completion.importOrder": ["java", "javax", "jakarta", "org", "com", "io"]
   ```

## Verificação no CI/CD

O pipeline de CI/CD está configurado para verificar o estilo de código usando:

```bash
mvn clean checkstyle:check impsort:check
```
