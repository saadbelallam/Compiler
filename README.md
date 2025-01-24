# Deca compiler

A compiler for Deca, a small object-oriented language close to a subset of Java, written in Java. It takes `.deca` source files and produces assembly for IMA.
Built by a team of 5.

## What it does

The compiler goes through the three classic stages:

1. **Syntax analysis.** Lexer and parser written with ANTLR4, producing an abstract syntax tree.
2. **Contextual analysis.** Type checking, scopes, class hierarchy, method overriding rules, return types. Errors come with the file, line and column.
3. **Code generation.** Walks the tree and emits IMA assembly: register allocation, stack and heap management, virtual method tables for dynamic dispatch, runtime checks for overflow, division by zero, null dereference and stack overflow.

It supports the full object language: classes, inheritance, fields with visibility, methods with overriding, `this`, `new`, `instanceof`, `null`, and casts between `int` and `float`.

Example:

```java
class Counter {
    int value;

    void add(int n) {
        this.value = this.value + n;
    }
}

{
    Counter c = new Counter();
    c.add(3);
    c.add(4);
    println("value = ", c.value);
}
```

## Usage

```
decac [[-p | -v] [-n] [-r X] [-d]* [-P] [-w] <file.deca>...] | [-b]
```

| Option | What it does |
|--------|--------------|
| `-p`   | stop after parsing and print the decompiled tree |
| `-v`   | stop after contextual analysis, print nothing if the program is valid |
| `-r X` | limit the compiler to registers R0 to R{X-1} (4 ≤ X ≤ 16) |
| `-d`   | debug traces, repeat for more detail |
| `-P`   | compile several files in parallel |
| `-b`   | print the team banner |

The output `.ass` file is written next to the source file.

## Build and run

Requires Java 17+ and Maven.

```bash
mvn compile
src/main/bin/decac path/to/program.deca
```

To run the generated assembly you need `ima`, the Ensimag abstract machine. It isn't distributed publicly, so outside the school you can compile and inspect the `.ass` output but not execute it.

## Testing

We had a rule that whoever wrote a part didn't write its tests. That gave us 334 Deca test programs split by stage (`src/test/deca/{lex,syntax,context,codegen}`), each with valid and invalid cases, plus JUnit 5 and Mockito unit tests. Coverage was tracked with JaCoCo.

```bash
mvn test
src/test/script/test-codegen.sh   # or test-lex, test-synt, test-context, test-options
```
## Extension

We also wrote a trigonometry library in Deca itself (`sin`, `cos`, `tan`, `atan`, `ulp`), comparing Taylor series, Chebyshev polynomials and CORDIC, with a precision analysis in ULP. It lives in `src/main/resources/include/Math.decah`.

Tools: Java, ANTLR4, Maven, JUnit 5, Mockito, JaCoCo. Organised in 4 Scrum sprints.
