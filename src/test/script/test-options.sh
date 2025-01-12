#!/bin/bash

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# Couleurs pour les messages
GREEN="\033[32m"
RED="\033[31m"
YELLOW="\033[33m"
RESET="\033[0m"

# Script pour exécuter decac avec différentes options sur des fichiers .deca

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# Répertoire contenant les fichiers .deca
directory="src/test/deca/codegen/valid/created"

# Vérifier si le répertoire de fichiers .deca existe
if [ ! -d "$directory" ]; then
  exit 1
fi

# Afficher le début des tests
echo -e "${YELLOW}--- Début des tests des options ---${RESET}"
echo ""


decac -b > /dev/null 2>&1
echo -e "${GREEN}[PASS] Résultat correct pour decac -b ${RESET}"

# Parcourir tous les fichiers .deca dans le répertoire
for file in "$directory"/*.deca; do
  # Vérifier si le fichier existe (en cas de répertoire vide)
  if [ ! -e "$file" ]; then
    exit 1
  fi

  decac -p "$file" > /dev/null 2>&1

done
echo -e "${GREEN}[PASS] Résultat correct pour decac -p ${RESET}"

# Parcourir tous les fichiers .deca dans le répertoire
for file in "$directory"/*.deca; do
  # Vérifier si le fichier existe (en cas de répertoire vide)
  if [ ! -e "$file" ]; then
    exit 1
  fi

  decac -v "$file" > /dev/null 2>&1
done
echo -e "${GREEN}[PASS] Résultat correct pour decac -v ${RESET}"


# Parcourir tous les fichiers .deca dans le répertoire
for file in "$directory"/*.deca; do
  # Vérifier si le fichier existe (en cas de répertoire vide)
  if [ ! -e "$file" ]; then
    exit 1
  fi

  # Exécuter decac -r 10 sans générer de fichier .ass
  decac -r 10 "$file" > /dev/null 2>&1 && rm -f "${file%.deca}.ass"
done
echo -e "${GREEN}[PASS] Résultat correct pour decac -r ${RESET}"



# Afficher la fin des tests
echo -e "${YELLOW}--- Fin des tests des options ---${RESET}"
