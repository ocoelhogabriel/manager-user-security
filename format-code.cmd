@echo off
REM Script para formatar código e organizar importações

REM Verifica se o Maven está disponível
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Maven não encontrado. Tentando usar o wrapper...
    if exist ".\mvnw.cmd" (
        set MVN=.\mvnw.cmd
    ) else (
        echo Nem Maven nem mvnw.cmd encontrados. Por favor, instale o Maven ou adicione o wrapper.
        exit /b 1
    )
) else (
    set MVN=mvn
)

echo Iniciando formatação e organização de importações...

REM Executa o plugin para organizar importações
echo Organizando importações com o plugin Impsort...
%MVN% impsort:sort

REM Executa o formatter para formatar código
echo Formatando código com o plugin formatter...
%MVN% formatter:format

echo Formatação e organização de importações concluídas com sucesso!