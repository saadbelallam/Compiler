#!/bin/sh

# Author: gl24
# Initial version: 01/01/2025

# Test code generation for a single file

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:./src/main/bin:"$PATH"

# Clean up any existing assembly file
rm -f ./src/test/deca/codegen/valid/provided/cond0.ass 2>/dev/null

# Run code generation with debug output
decac ./src/test/deca/codegen/valid/provided/cond0.deca || exit 1

# Check if assembly file was generated
if [ ! -f ./src/test/deca/codegen/valid/provided/cond0.ass ]; then
    echo "Error: code0.ass was not generated."
    exit 1
fi

echo "Code generation successful - assembly file created"
echo "Assembly file location: ./src/test/deca/codegen/valid/provided/code0.ass"

# Display generated assembly code
echo "Generated assembly code:"
cat ./src/test/deca/codegen/valid/provided/code0.ass
