#!/bin/bash

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# Couleurs pour les messages
GREEN="\033[32m"
RED="\033[31m"
YELLOW="\033[33m"
RESET="\033[0m"

# Variables pour le résumé
success_count=0
fail_count=0

# Fonction pour tester les fichiers invalides
test_synt_invalide() {
    # $1 = chemin du fichier
    if test_synt "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"; then
        echo -e "${GREEN}[PASS] Échec attendu détecté pour $1.${RESET}"
        ((success_count++))
    else
        echo -e "${RED}[FAIL] Succès inattendu pour $1.${RESET}"
        ((fail_count++))
    fi
}

# Fonction pour tester les fichiers valides
test_synt_valid() {
    # $1 = chemin du fichier
    if test_synt "$1" 2>&1 | grep -q -e ":[0-9][0-9]*:"; then
        echo -e "${RED}[FAIL] Erreur détectée dans un fichier valide : $1.${RESET}"
        test_synt "$1" 2>&1
        ((fail_count++))
    else
        echo -e "${GREEN}[PASS] Aucun problème détecté pour $1.${RESET}"
        ((success_count++))
    fi
}

# En-tête des tests invalides
echo ""
echo -e "${YELLOW}--- Début des tests de syntaxe invalides ---${RESET}"

# Boucle sur tous les fichiers de test invalides
for cas_de_test in src/test/deca/syntax/invalid/provided/*.deca; do
    test_synt_invalide "$cas_de_test"
done


# En-tête des tests valides
echo ""
echo -e "${YELLOW}--- Début des tests de syntaxe valides ---${RESET}"

# Boucle sur tous les fichiers de test valides
for cas_de_test in src/test/deca/syntax/valid/provided/*.deca; do
    test_synt_valid "$cas_de_test"
done


# Résumé final
echo ""
echo -e "${YELLOW}--- Résumé des tests ---${RESET}"
echo -e "${GREEN}Tests réussis : $success_count${RESET}"
echo -e "${RED}Tests échoués : $fail_count${RESET}"
echo ""


