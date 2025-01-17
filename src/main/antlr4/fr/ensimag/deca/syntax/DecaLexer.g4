lexer grammar DecaLexer;

options {
   language=Java;
   superClass = AbstractDecaLexer;
}

@members {
}

// Keywords must come BEFORE IDENT to have higher priority
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

// Operators
EQEQ     : '==';
NEQ      : '!=';
GEQ      : '>=';
LEQ      : '<=';
LT       : '<';
GT       : '>';
AND      : '&&';
OR       : '||';
EQUALS   : '=';
PLUS     : '+';
MINUS    : '-';
TIMES    : '*';
SLASH    : '/';
PERCENT  : '%';

// Symbols
COMMA    : ',';
DOT      : '.';
OPARENT  : '(';
CPARENT  : ')';
OBRACE   : '{';
CBRACE   : '}';
EXCLAM   : '!';
SEMI     : ';';

// Identifiers and numbers
fragment LETTER : 'a'..'z' | 'A'..'Z';
fragment DIGIT : '0'..'9';
fragment POSITIVE_DIGIT : '1'..'9';
IDENT : (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')*;
INT : '0' | POSITIVE_DIGIT DIGIT*;

// Float literals
fragment NUM : DIGIT+;
fragment SIGN : '+' | '-';
fragment EXP : ('E' | 'e') SIGN? NUM;
fragment DEC : NUM '.' NUM;
fragment FLOATDEC : DEC (EXP)? ('F' | 'f')?;
fragment DIGITHEX : '0'..'9' | 'A'..'F' | 'a'..'f';
fragment NUMHEX : DIGITHEX+;
fragment FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN? NUM ('F' | 'f')?;
FLOAT : FLOATDEC | FLOATHEX;

// String handling
fragment STRING_CAR : ~('"'|'\r'|'\n'|'\\') ;
fragment EOL : '\n' | '\r' | '\r\n';
fragment ESC : '\\' ['"\\] ;

// Modified string rules to handle escapes properly
STRING : '"' (STRING_CAR | ESC)* '"';
MULTI_LINE_STRING : '"' (STRING_CAR | ESC | EOL)* '"';

// Comments
COMMENT : ('/*' .*? '*/' | '//' ~[\r\n]*) -> skip;

// Include directive
fragment FILENAME : (LETTER | DIGIT | DOT | MINUS | '_')+;
INCLUDE : '#include' [ \t]* '"' FILENAME '"' {doInclude(getText());};

// Whitespace
WS : [ \t\r\n] -> skip;