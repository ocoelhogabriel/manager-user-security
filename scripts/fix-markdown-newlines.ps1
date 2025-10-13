# Script para garantir que todos os arquivos Markdown tenham uma linha em branco no final
# Este script verifica e corrige os arquivos Markdown que não terminam com uma linha em branco
# Criado como parte do processo de padronização da documentação do projeto Manager User Security

# Configuração
$docsPath = "d:\developer\repositorys\OCoelhoGabriel\manager_user_security\docs"
$filePattern = "*.md"
$reportPath = "d:\developer\repositorys\OCoelhoGabriel\manager_user_security\scripts\markdown-newlines-report.txt"

# Contadores para relatório
$totalFiles = 0
$fixedFiles = 0
$alreadyCorrectFiles = 0

# Criar ou limpar o arquivo de relatório
"Relatório de Verificação de Linhas em Branco em Arquivos Markdown" | Out-File -FilePath $reportPath
"Gerado em: $(Get-Date)" | Out-File -FilePath $reportPath -Append
"=============================================================" | Out-File -FilePath $reportPath -Append
"" | Out-File -FilePath $reportPath -Append

# Encontrar todos os arquivos Markdown recursivamente
$markdownFiles = Get-ChildItem -Path $docsPath -Recurse -Include $filePattern

$totalFiles = $markdownFiles.Count
Write-Host "Encontrados $totalFiles arquivos Markdown"
"Total de arquivos encontrados: $totalFiles" | Out-File -FilePath $reportPath -Append
"" | Out-File -FilePath $reportPath -Append
"Arquivos corrigidos:" | Out-File -FilePath $reportPath -Append
"-------------------" | Out-File -FilePath $reportPath -Append

foreach ($file in $markdownFiles) {
    $content = Get-Content -Path $file.FullName -Raw
    $relativePath = $file.FullName.Replace($docsPath + "\", "")
    
    # Verificar se o arquivo já termina com uma nova linha
    if ($content -match "[^\r\n]$") {
        Write-Host "Corrigindo arquivo: $relativePath"
        
        # Adicionar uma linha em branco no final
        Add-Content -Path $file.FullName -Value ""
        
        # Atualizar contadores e relatório
        $fixedFiles++
        $relativePath | Out-File -FilePath $reportPath -Append
    } else {
        Write-Host "Arquivo já está correto: $relativePath"
        $alreadyCorrectFiles++
    }
}

"" | Out-File -FilePath $reportPath -Append
"Resumo:" | Out-File -FilePath $reportPath -Append
"-------" | Out-File -FilePath $reportPath -Append
"Total de arquivos: $totalFiles" | Out-File -FilePath $reportPath -Append
"Arquivos corrigidos: $fixedFiles" | Out-File -FilePath $reportPath -Append
"Arquivos já corretos: $alreadyCorrectFiles" | Out-File -FilePath $reportPath -Append

Write-Host ""
Write-Host "Processamento concluído."
Write-Host "Total de arquivos: $totalFiles"
Write-Host "Arquivos corrigidos: $fixedFiles"
Write-Host "Arquivos já corretos: $alreadyCorrectFiles"
Write-Host "Relatório salvo em: $reportPath"