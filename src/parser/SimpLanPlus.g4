grammar SimpLanPlus ;

prog   : exp                                                            #simpleProg
       | (dec)+ (stm)* (exp)?                                           #complexProg
       ;

dec    : type ID ';'                                                    #varDec
       | type ID '(' ( param ( ',' param)* )? ')' '{' body '}'          #funDec
       ;

param  : type ID ;

body   : (dec)* (stm)* (exp)?
	   ;

type   : 'int'
       | 'bool'
       | 'void'
       ;

stm    : ID '=' exp ';'                                                  #initStm
       | ID '(' (exp (',' exp)* )? ')' ';'                               #funStm
       | 'if' '(' exp ')' '{' (stm)+ '}' ('else' '{' (stm)+ '}')?        #ifStm
	   ;

exp    : INTEGER                                                         #intExp
       | ( 'true' | 'false' )                                            #boolExp
       | ID                                                              #varExp
       | '!' exp                                                         #notExp
       | exp ('*' | '/') exp                                             #extendedArithmeticExp
       | exp ('+' | '-') exp                                             #baseArithmeticExp
       | exp ('>' | '<' | '>=' | '<=' | '==') exp                        #realtionalExp
       | exp ('&&' | '||') exp                                           #logicalExp
       | 'if' '(' exp ')' '{' (stm)* exp '}' 'else' '{' (stm)* exp '}'   #ifExp
       | '(' exp ')'                                                     #bracketsExp
       | ID '(' (exp (',' exp)* )? ')'                                   #funExp
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