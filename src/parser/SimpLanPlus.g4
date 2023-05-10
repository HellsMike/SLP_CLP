grammar SimpLanPlus ;

prog   : exp
       | (dec)+ (stm)* (exp)?
       ;

dec    : type ID ';'
       | type ID '(' ( param ( ',' param)* )? ')' '{' body '}'
       ;

param  : type ID ;

body   : (dec)* (stm)* (exp)?
	   ;

type   : 'int'
       | 'bool'
       | 'void'
       ;

stm    : ID '=' exp ';'
       | ID '(' (exp (',' exp)* )? ')' ';'
       | 'if' '(' exp ')' '{' (stm)+ '}' ('else' '{' (stm)+ '}')?
	   ;

exp    :  INTEGER | 'true' | 'false'
       | ID
       | '!' exp
       | exp ('*' | '/') exp
       | exp ('+' | '-') exp
       | exp ('>' | '<' | '>=' | '<=' | '==') exp
       | exp ('&&' | '||') exp
       | 'if' '(' exp ')' '{' (stm)* exp '}' 'else' '{' (stm)* exp '}'
       | '(' exp ')'
       | ID '(' (exp (',' exp)* )? ')'
       ;

/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

//Numbers
fragment DIGIT  : '0'..'9';
INTEGER         : DIGIT+;

//IDs
fragment CHAR   : 'a'..'z' |'A'..'Z' ;
ID              : CHAR (CHAR | DIGIT)* ;

//ESCAPE SEQUENCES
WS              : (' '|'\t'|'\n'|'\r')-> skip;
LINECOMENTS     : '//' (~('\n'|'\r'))* -> skip;
BLOCKCOMENTS    : '/*'( ~('/'|'*')|'/'~'*'|'*'~'/'|BLOCKCOMENTS)* '*/' -> skip;

ERR             : .  -> channel(HIDDEN);