#!/bin/bash

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# Couleurs pour les messages
GREEN="\033[32m"
RED="\033[31m"
YELLOW="\033[33m"
RESET="\033[0m"


# Répertoire contenant les fichiers .deca
directory="src/test/deca/codegen/valid/created"
# Répertoire où les fichiers .ass seront copiés
output_directory="src/test/deca/codegen/valid/generated"


# Vérifier si le répertoire de sortie existe, sinon le créer
if [ ! -d "$output_directory" ]; then
  mkdir -p "$output_directory"
fi

# Parcourir tous les fichiers .deca dans le répertoire
for file in "$directory"/*.deca; do
  # Vérifier si des fichiers .deca existent
  if [ ! -e "$file" ]; then
    echo -e "${YELLOW}Aucun fichier .deca trouvé dans $directory.${RESET}"
    exit 1
  fi

  
  # Exécuter la commande decac
  decac "$file"

  # Vérifier si la commande a échoué
  if [ $? -ne 0 ]; then
    echo -e "${RED}Erreur lors de la compilation de $file ${RESET}"
    exit 1
  fi

  # Nom du fichier .ass généré
  ass_file="${file%.deca}.ass"

  # Vérifier si le fichier .ass existe
  if [ -f "$ass_file" ]; then
    # Copier le fichier .ass dans le répertoire de destination
    mv "$ass_file" "$output_directory"

  fi
done

