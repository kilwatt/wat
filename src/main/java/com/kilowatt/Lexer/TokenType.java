package com.kilowatt.Lexer;

/*
Тип токена
 */
public enum TokenType {
    FUN,
    OPERATOR, // +, -, *, /
    LPAREN,
    RPAREN,
    LBRACE,
    RBRACE,
    LAMBDA, // lambda
    WALRUS, // :=
    EQUAL, // ==
    NOT_EQUAL, // !=
    TEXT, // 'text'
    NUM, // 1234567890.0123456789
    ASSIGN, // =
    ID, // variable id
    COMMA, // ,
    RETURN, // return
    IF, // if
    BOOL, // bool
    WHILE, // while
    TYPE, // type
    NEW, // new
    DOT, // dot
    GREATER, // >
    LESS,  // <
    GREATER_EQ, // >=
    LESS_EQ, // <=
    NULL, // null
    ELIF, // elif
    ELSE, // else
    AND, // logical and
    OR, // logical or
    IMPORT, // import
    ASSIGN_ADD, // assign add
    ASSIGN_SUB, // assign sub
    ASSIGN_MUL, // assign mul
    ASSIGN_DIVIDE,  // assign divide
    BREAK, // break
    MATCH, // match
    CASE, // case
    DEFAULT, // default
    LBRACKET, // [
    RBRACKET, // ]
    COLON, // colon :
    FOR, // for
    TERNARY, // ternary
    IN, // in
    CONTINUE, // continue
    TRY, // try
    CATCH, // catch
    THROW, // throw
    ARROW, // ->
    UNIT, // unit
    TO, // to
    FROM, // from
    NATIVE, // native
    PIPE, // pipe
    WITH, // with
    RANGE // range
}
