grammar AggregatorWtal;

//#region PARSER RULES

start: statement+ EOF ;

statement:
    IDENTIFIER ASSIGN expression SEMI                                                                              #AssignStatement
    | expression SEMI                                                                                              #EvalStatement
    ;

expression
    : POW LPAREN expression COMMA expression RPAREN                                                                # PowFunction
    | SQRT LPAREN expression RPAREN                                                                                # SqrtFunction 
    | MAP LPAREN expression COMMA lambda_expression RPAREN                                                         # MapFunction
    | FILTER LPAREN expression COMMA lambda_expression RPAREN                                                      # FilterFunction
    | ISNULL LPAREN expression RPAREN                                                                              # IsNullFunction
    | TERNARY LPAREN expression COMMA expression COMMA expression RPAREN                                           # TernaryFunction
    | LPAREN expression RPAREN                                                                                     # Parenthesis
    | MINUS expression                                                                                             # UnaryMinus
    | expression LBRACKET expression (COLON expression)? RBRACKET                                                  # Accesor
    | expression op=(MULT | DIV) expression                                                                        # MulDiv
    | expression op=(PLUS | MINUS) expression                                                                      # PlusMinus
    | expression op=(AND | OR) expression                                                                          # AndOr
    | expression op=MOD expression                                                                                 # Mod
    | expression op=(GREATER_THAN | LESS_THAN | EQUALS | DISTINCT | GREATER_OR_EQUALS | LESS_OR_EQUALS) expression # Comparators
    | expression op=IN expression                                                                                  # In
    | atom                                                                                                         # Atomic
    ;

atom
    : NUMBER
    | IDENTIFIER
    | STRING
    | BOOLEAN
    | json_object
    | json_array
    ;

lambda_expression
    : IDENTIFIER ARROW expression
    ;

json_object
    : LBRACE (json_pair (COMMA json_pair)*)? RBRACE
    ;

json_pair
    : STRING COLON json_value
    ;

json_array
    : LBRACKET (json_value (COMMA json_value)*)? RBRACKET
    ;

json_value
    : atom
    | json_object
    | json_array
    ;

//#endregion

//#region LEXER RULES

// Operators
PLUS: '+' ;
MINUS: '-' ;
MULT: '*' ;
DIV: '/' ;
AND: '&' ;
OR: '|' ;
ASSIGN: '=' ;
ARROW: '->' ;
GREATER_THAN: '>' ;
LESS_THAN: '<' ;
EQUALS: '==' ;
DISTINCT: '!=' ;
GREATER_OR_EQUALS: '>=' ;
LESS_OR_EQUALS: '<=' ;
MOD: '%' ;
IN: 'IN' ;

// Line separator
SEMI: ';' ;

// Functions
POW: 'POW' ;
SQRT: 'SQRT' ;
MAP: 'MAP' ;
FILTER: 'FILTER' ;
ISNULL: 'ISNULL' ;
TERNARY: 'TERNARY' ;

// Numbers
NUMBER:
    DIGIT+ (POINT DIGIT+)? ;

// Util tokens
POINT: '.' ;
COMMA: ',' ;
LBRACKET: '[' ;
RBRACKET: ']' ;
LBRACE: '{' ;
RBRACE: '}' ;
COLON: ':' ;
LPAREN: '(' ;
RPAREN: ')' ;

// Skip characters
SPACE: [ \t\r\n] -> skip ;

STRING:
    '"' (~[\r\n"])* '"' ;

BOOLEAN: 'true' | 'false' ;

// Variables
IDENTIFIER:
    (LETTER | UNDERSCORE) (LETTER | DIGIT | UNDERSCORE)* ;

// Reusable components
fragment DIGIT: [0-9] ;
fragment LETTER: [a-zA-Z] ;
fragment UNDERSCORE: '_' ;

//#endregion