package com.kilowatt.Lexer;

/*
Тип токена
 */
public enum TokenType {
    FUN,
    OPERATOR, // +, -, *, /
    LEFT_PAREN,
    RIGHT_PAREN,
    LEFT_BRACE,
    RIGHT_BRACE,
    CALL, // @
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
    BIGGER, // >
    LOWER,  // <
    BIGGER_EQUAL, // >=
    LOWER_EQUAL, // <=
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
    LEFT_BRACKET, // [
    RIGHT_BRACKET, // ]
    COLON, // colon :
    FOR, // for
    TERNARY, // ternary
    IN, // in
    ASSERT, // assert
    CONTINUE, // continue
    TRY, // try
    CATCH, // catch
    THROW, // throw
    GO, // ->
    UNIT, // unit
    TO, // to
    FROM, // from
    NATIVE // native
}
