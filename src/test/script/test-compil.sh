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

# Répertoires
generated_dir="src/test/deca/codegen/valid/generated"
results_dir="src/test/deca/codegen/valid/results"
invalid_dir="src/test/deca/codegen/invalid/generated"

# Fonction pour exécuter ima sur un fichier .ass et comparer avec le résultat attendu
test_ima_result() {
    local ass_file=$1
    local file_name
    file_name=$(basename "$ass_file" .ass)
    local expected_result_file="$results_dir/$file_name.res"

    # Vérification de l'existence du fichier de résultat attendu
    if [ ! -f "$expected_result_file" ]; then
        echo -e "${YELLOW}[WARN] Fichier de résultat attendu non trouvé pour $file_name.${RESET}"
        ((fail_count++))
        return
    fi

    # Exécution de ima
    local result
    result=$(ima "$ass_file" 2>&1) || {
        echo -e "${RED}[FAIL] Erreur lors de l'exécution de ima sur $file_name.ass.${RESET}"
        ((fail_count++))
        return
    }

    # Lecture du résultat attendu
    local expected_result
    expected_result=$(<"$expected_result_file")

    # Comparaison du résultat obtenu avec le résultat attendu
    if [ "$result" = "$expected_result" ]; then
        echo -e "${GREEN}[PASS] Résultat correct pour $file_name.deca.${RESET}"
        ((success_count++))
    else
        echo -e "${RED}[FAIL] Résultat incorrect pour $file_name.deca.${RESET}"
        echo "Obtenu : $result"
        echo "Attendu : $expected_result"
        ((fail_count++))
    fi
}

# Fonction principale pour parcourir et tester les fichiers .ass
run_tests_on_generated() {
    echo ""
    echo -e "${YELLOW}--- Début des tests sur les fichiers générés ---${RESET}"

    for ass_file in "$generated_dir"/*.ass; do
        if [ -f "$ass_file" ]; then
            test_ima_result "$ass_file"
        else
            echo -e "${YELLOW}[WARN] Aucun fichier .ass trouvé dans $generated_dir.${RESET}"
        fi
    done

    echo ""
}

# Fonction pour tester les fichiers invalides
run_invalid_test() {
    echo ""
    echo -e "${YELLOW}--- Début des tests sur les fichiers invalides ---${RESET}"

    for invalid_file in "$invalid_dir"/*.ass; do
        if [ -f "$invalid_file" ]; then
            # Exécuter la commande decac
            result=$(ima "$invalid_file" 2>&1)
	if echo "$result" | grep -q -e "Error" -e "\*\* IMA \*\*" -e "Erreur : dereferencement de null"; then

                echo -e "${GREEN}[PASS] Erreur détectée comme attendu pour ${invalid_file%.ass}.deca.${RESET}"
                ((success_count++))
            else
                echo -e "${RED}[FAIL] Pas d'erreur détectée ou erreur inattendue pour ${invalid_file%.ass}.deca.${RESET}"
                echo "Sortie obtenue :"
                echo "$result"
                ((fail_count++))
            fi
        else
            echo -e "${YELLOW}[WARN] Aucun fichier .ass trouvé dans $invalid_dir.${RESET}"
        fi
    done

    echo ""
}

# Fonction pour afficher le résumé des tests
show_summary() {
    echo ""
    echo -e "${YELLOW}--- Résumé des tests ---${RESET}"
    echo -e "${GREEN}Tests réussis : $success_count${RESET}"
    echo -e "${RED}Tests échoués : $fail_count${RESET}"
    echo ""
}

# Supprimer les répertoires générés
rm -rf "$generated_dir"
rm -rf "$results_dir"
rm -rf "$invalid_dir"

# Lancement des tests
if [ ! -d "$generated_dir" ] || [ ! -d "$results_dir" ]; then
    ./src/test/script/run-test.sh
fi

# Tests sur les fichiers générés
run_tests_on_generated

# Tests sur les fichiers invalides
run_invalid_test

# Afficher le résumé
show_summary

# Fin du script
exit 0
