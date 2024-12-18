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

// lexer pour langage Hello-world
// permet l'affichage des chaînes de caractères
// Bzzaf zaydine hna ghi bach n7yde les erreurs f le parser

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

// hado ghi zrbte 3lihom bach ymchiw les erreurs mn l parser
// Symboles composés
EQ      : '==';
NEQ     : '!=';
GEQ     : '>=';
LEQ     : '<=';
AND     : '&&';
OR      : '||';

// Symboles simples
LT      : '<';
GT      : '>';
EQUALS  : '=';
PLUS    : '+';
MINUS   : '-';
TIMES   : '*';
SLASH   : '/';
PERCENT : '%';
DOT     : '.';
COMMA   : ',';
OPARENT  : '(';
CPARENT  : ')';
OBRACE  : '{';
CBRACE  : '}';
EXCLAM  : '!';
SEMI    : ';';
EQEQ    : '===';

//Identificateurs
fragment LETTER : 'a' .. 'z' | 'A' .. 'Z';
fragment DIGIT : '0' .. '9';
fragment POSITIVE_DIGIT : '1' .. '9';
IDENT : (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')*;
INT : '0' | POSITIVE_DIGIT DIGIT*;

// Lttéraux flottants
fragment NUM : DIGIT+;
fragment SIGN:  '+' | '-' | ' ';
fragment EXP : ('E' | 'e') SIGN NUM;
fragment DEC : NUM '.' NUM;
fragment FLOATDEC : (DEC | DEC EXP) ('F' | 'f' | ' ');
fragment DIGITHEX : '0'  ..  '9' | 'A' .. 'F' | 'a' .. 'f';
fragment NUMHEX : DIGITHEX+;
fragment FLOATHEX : ('0x' + '0X') NUMHEX '.' ('P' | 'p') SIGN NUM ('F' | 'f' | ' ');
FLOAT : FLOATDEC | FLOATHEX;
// fragment STRING_CAR contient tous les carctères
// à l’exception des caractères '"', '\' et de EOL
fragment STRING_CAR : ~["\\\r\n] ;

// Caractère de formatage
EOL : '\r' | '\n';


// String
STRING : '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING : '"' (STRING_CAR | EOL | '\\"' | '\\\\')* '"';



// Ignore spaces, tabs, newlines and whitespaces
WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {
              skip(); // avoid producing a token
          }
    ;
