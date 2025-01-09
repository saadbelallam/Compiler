#!/bin/bash

# decac + ima + stock les resultats

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


# Répertoire source des fichiers .ass
input_dir="src/test/deca/codegen/valid"
# Répertoire de sortie pour les fichiers .res
output_dir="src/test/deca/codegen/valid/results"


./src/test/script/test-codegen.sh

# Crée le répertoire de résultats s'il n'existe pas déjà
mkdir -p "$output_dir"
# Parcours de tous les fichiers .ass dans les sous-répertoires du répertoire de test
for ass_file in "$input_dir"/*/*.ass; do
    # Récupère le nom du fichier sans le chemin
    file_name=$(basename "$ass_file" .ass)

    # Exécute la machine "ima" sur le fichier .ass et redirige la sortie vers un fichier .res
    ima "$ass_file" > "$output_dir/$file_name.res"
    
done

