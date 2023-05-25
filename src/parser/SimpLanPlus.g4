grammar SimpLanPlus ;

prog   : exp                                                                                             #simpleProg
       | (dec)+ (stm)* (exp)?                                                                            #complexProg
       ;

dec    : type ID ';'                                                                                     #varDec
       | type ID '(' ( param ( ',' param)* )? ')' '{' body '}'                                           #funDec
       ;

param  : type ID
       ;

body   : (dec)* (stm)* (exp)?
	   ;

type   : 'int'
       | 'bool'
       | 'void'
       ;

stm    : ID '=' exp ';'                                                                                   #initStm
       | ID '(' (exp (',' exp)* )? ')' ';'                                                                #funStm
       | 'if' '(' exp ')' then=ifBodyS  ('else' else=ifBodyS)?                                            #ifStm
	   ;

exp    : INTEGER                                                                                          #intExp
       | ( 'true' | 'false' )                                                                             #boolExp
       | ID                                                                                               #varExp
       | '!' exp                                                                                          #notExp
       | left=exp (mul='*' | div='/' | plus='+' | minus='-') right=exp                                    #arithmeticExp
       | left=exp (gr='>' | min='<' | greq='>=' | mineq='<=' | comp='==') right=exp                       #realtionalExp
       | left=exp (and='&&' | or='||') right=exp                                                          #logicalExp
       | 'if' '(' exp ')' then=ifBodyE 'else' else=ifBodyE                                                #ifExp
       | '(' exp ')'                                                                                      #bracketsExp
       | ID '(' (exp (',' exp)* )? ')'                                                                    #funExp
       ;

ifBodyE: '{' (stm)* exp '}'
       ;

ifBodyS: '{' (stm)* '}'
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