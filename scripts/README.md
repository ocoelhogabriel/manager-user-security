# Scripts de Manutenção de Documentação

Este diretório contém scripts para manter e padronizar a documentação do projeto Manager User Security.

## Scripts Disponíveis

### fix-markdown-newlines.ps1

Script PowerShell para garantir que todos os arquivos Markdown tenham uma linha em branco no final, conforme as boas práticas de formatação.

#### Uso

```powershell
# No PowerShell
.\fix-markdown-newlines.ps1
```

#### Funcionalidades

- Verifica recursivamente todos os arquivos `.md` no diretório de documentação
- Adiciona uma linha em branco no final dos arquivos que não têm
- Gera um relatório detalhado do processamento

#### Relatório

O script gera um relatório em `markdown-newlines-report.txt` com as seguintes informações:

- Data e hora da execução
- Total de arquivos verificados
- Lista de arquivos corrigidos
- Resumo com contagem de arquivos corretos e corrigidos

## Uso com Git Hooks

Para integrar com Git Hooks e garantir que todos os arquivos Markdown estejam formatados corretamente antes do commit:

1. Crie um arquivo `.git/hooks/pre-commit` com o seguinte conteúdo:

```bash
#!/bin/bash
# Execute o script de verificação de linhas em branco
pwsh -File ./scripts/fix-markdown-newlines.ps1

# Se o script modificou algum arquivo, adicione os arquivos modificados
git diff --name-only | grep '\.md$' | xargs git add
```

1. Torne o hook executável:

```bash
chmod +x .git/hooks/pre-commit
```
