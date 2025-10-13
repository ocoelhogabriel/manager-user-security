#!/bin/bash
# Script para formatar código e organizar importações

# Verifica se o Maven está disponível
if ! command -v mvn &> /dev/null; then
    echo "Maven não encontrado. Tentando usar o wrapper..."
    if [ -f "./mvnw" ]; then
        MVN="./mvnw"
    else
        echo "Nem Maven nem mvnw encontrados. Por favor, instale o Maven ou adicione o wrapper."
        exit 1
    fi
else
    MVN="mvn"
fi

# Cores para output
GREEN="\033[0;32m"
YELLOW="\033[0;33m"
RED="\033[0;31m"
NC="\033[0m" # No Color

echo -e "${YELLOW}Iniciando formatação e organização de importações...${NC}"

# Executa o plugin para organizar importações
echo -e "${YELLOW}Organizando importações com o plugin Impsort...${NC}"
$MVN impsort:sort

# Executa o formatter para formatar código
echo -e "${YELLOW}Formatando código com o plugin formatter...${NC}"
$MVN formatter:format

echo -e "${GREEN}Formatação e organização de importações concluídas com sucesso!${NC}"