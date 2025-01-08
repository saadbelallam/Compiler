lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

// ce bloc nous permet d'ajouter du cide au lexer
@members {
}

// Deca lexer rules.


// Mots réservés
ASM         : 'asm';
CLASS       : 'class';
EXTENDS     : 'extends';
ELSE        : 'else';
FALSE       : 'false';
IF          : 'if';
INSTANCEOF  : 'instanceof';
NEW         : 'new';
NULL        : 'null';
READINT     : 'readInt';
READFLOAT   : 'readFloat';
PRINT       : 'print';
PRINTLN     : 'println';
PRINTX      : 'printx';
PRINTLNX    : 'printlnx';
PROTECTED   : 'protected';
RETURN      : 'return';
THIS        : 'this';
TRUE        : 'true';
WHILE       : 'while';

// Opérateurs comparatifs
EQEQ     : '==';
NEQ     : '!=';
GEQ     : '>=';
LEQ     : '<=';
LT      : '<';
GT      : '>';

// Opérateurs logiques
AND     : '&&';
OR      : '||';

// Assignation
EQUALS  : '=';

// Opérateurs arithmétiques
PLUS    : '+';
MINUS   : '-';
TIMES   : '*';
SLASH   : '/';
PERCENT : '%';

// Symboles divers
COMMA   : ',';
DOT     : '.';
OPARENT : '(';
CPARENT : ')';
OBRACE  : '{';
CBRACE  : '}';
EXCLAM  : '!';
SEMI    : ';';


// Identificateurs
fragment LETTER : 'a' .. 'z' | 'A' .. 'Z';
fragment DIGIT : '0' .. '9';
fragment POSITIVE_DIGIT : '1' .. '9';
IDENT : (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')*;
INT : '0' | POSITIVE_DIGIT DIGIT*;

// Littéraux flottants
fragment NUM : DIGIT+;
fragment SIGN : '+' | '-';
fragment EXP : ('E' | 'e') SIGN? NUM;
fragment DEC : NUM '.' NUM;
fragment FLOATDEC : DEC (EXP)? ('F' | 'f')?;
fragment DIGITHEX : '0' .. '9' | 'A' .. 'F' | 'a' .. 'f';
fragment NUMHEX : DIGITHEX+;
fragment FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN? NUM ('F' | 'f')?;

// Définition principale pour les flottants
FLOAT : FLOATDEC | FLOATHEX;


// fragment STRING_CAR contient tous les carctères
// à l’exception des caractères '"', '\' et de EOL
fragment STRING_CAR : ~('"'|'\r'|'\n'|'\\') ;

// End of line (EOL)
fragment EOL: '\\' ('n'|'t'|'r');


// String
STRING : '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING : '"' (STRING_CAR | EOL | '\\"' | '\\\\')* '"';
COMMENT :  ('//' .*? '\n' | '/*' .*? '*/' ) -> skip;


// Ignore spaces, tabs, newlines and whitespaces
WS : (' '|'\t'|'\r'|'\n') -> skip;
