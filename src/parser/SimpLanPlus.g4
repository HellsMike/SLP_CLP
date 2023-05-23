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
       | 'if' '(' exp ')' '{' (thenStm=stm)+ '}' ('else' '{' (elseStm=stm)+ '}')?                         #ifStm
	   ;

exp    : INTEGER                                                                                          #intExp
       | ( 'true' | 'false' )                                                                             #boolExp
       | ID                                                                                               #varExp
       | '!' exp                                                                                          #notExp
       | left=exp (mul='*' | div='/' | plus='+' | minus='-') right=exp                                    #arithmeticExp
       | left=exp (gr='>' | min='<' | greq='>=' | mineq='<=' | comp='==') right=exp                       #realtionalExp
       | left=exp (and='&&' | or='||') right=exp                                                          #logicalExp
       | 'if' '(' cond=exp ')' '{' (thenStm=stm)* thenExp=exp '}' 'else' '{' (elseStm=stm)* elseExp=exp '}'   #ifExp
       | '(' exp ')'                                                                                      #bracketsExp
       | ID '(' (exp (',' exp)* )? ')'                                                                    #funExp
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