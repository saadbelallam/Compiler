#!/bin/bash

# Script pour exécuter decac + ima + stocker les fichiers générés

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"


# Variables pour le résumé
success_count=0
fail_count=0

# Couleurs pour les messages
GREEN="\033[32m"
RED="\033[31m"
YELLOW="\033[33m"
RESET="\033[0m"

# Répertoires
input_dir="src/test/deca/codegen/interactive"
generated_dir="src/test/deca/codegen/interactive/generated"

# Vérifier si le répertoire de sortie existe, sinon le créer
if [ ! -d "$generated_dir" ]; then
  mkdir -p "$generated_dir"
fi

# Parcourir tous les fichiers .deca dans le répertoire input_dir
for file in "$input_dir"/*.deca; do
  # Vérifier si des fichiers .deca existent
  if [ ! -e "$file" ]; then
    echo -e "${YELLOW}Aucun fichier .deca trouvé dans $input_dir.${RESET}"
    exit 1
  fi

  # Exécuter la commande decac
  decac "$file"

  # Vérifier si la commande a échoué
  if [ $? -ne 0 ]; then
    echo -e "${RED}Erreur lors de la compilation de $file ${RESET}"
    continue
  fi

  # Nom du fichier .ass généré
  ass_file="${file%.deca}.ass"

  # Vérifier si le fichier .ass existe
  if [ -f "$ass_file" ]; then
    # Déplacer le fichier .ass dans le répertoire de destination
    mv "$ass_file" "$generated_dir"
  fi
done

# Exécuter ima sur les fichiers générés
for ass_file in "$generated_dir"/*.ass; do
  if [ ! -e "$ass_file" ]; then
    echo -e "${YELLOW}Aucun fichier .ass trouvé dans $generated_dir.${RESET}"
    exit 1
  fi

  # Exécuter la machine ima
  ima "$ass_file"
  
  # Vérifier si ima a rencontré une erreur
  if [ $? -ne 0 ]; then
    echo -e "${RED}[FAIL] Erreur lors de l'exécution de $ass_file${RESET}"
    ((fail_count++))
  else
    echo -e "${GREEN}[PASS] Exécution réussie : $(basename "$ass_file")${RESET}"
    ((success_count++))

  fi
done



echo ""
echo -e "${YELLOW}--- Résumé des tests ---${RESET}"
echo -e "${GREEN}Tests réussis : $success_count${RESET}"
echo -e "${RED}Tests échoués : $fail_count${RESET}"
echo ""


# Fin du script
exit 0
