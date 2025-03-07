package com.kilowatt.Lexer;

import com.kilowatt.Errors.WattParsingError;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
Новый лексер
 */
@Getter
public class Lexer {
    // токены
    // токен
    private final ArrayList<Token> tokens = new ArrayList<>();
    // имя файла
    private final String filename;
    // код
    private final String code;
    // текущие значения
    private int line = 1;
    private int current = 0;
    // кейворды
    private static final Map<String, TokenType> keywords = new HashMap<>() {{
        put("func", TokenType.FUNC);
        put("return", TokenType.RETURN);
        put("if", TokenType.IF);
        put("true", TokenType.BOOL);
        put("false", TokenType.BOOL);
        put("while", TokenType.WHILE);
        put("type", TokenType.TYPE);
        put("new", TokenType.NEW);
        put("null", TokenType.NULL);
        put("elif", TokenType.ELIF);
        put("else", TokenType.ELSE);
        put("and", TokenType.AND);
        put("or", TokenType.OR);
        put("import", TokenType.IMPORT);
        put("break", TokenType.BREAK);
        put("match", TokenType.MATCH);
        put("case", TokenType.CASE);
        put("_", TokenType.DEFAULT);
        put("for", TokenType.FOR);
        put("assert", TokenType.ASSERT);
        put("continue", TokenType.CONTINUE);
        put("try", TokenType.TRY);
        put("catch", TokenType.CATCH);
        put("throw", TokenType.THROW);
        put("in", TokenType.IN);
        put("unit", TokenType.UNIT);
        put("to", TokenType.TO);
        put("from", TokenType.FROM);
        put("native", TokenType.NATIVE);
    }};

    // сканнер
    public Lexer(String filename, String code)
    {
        this.filename = filename;
        this.code = code;
    }

    // скан кода
    public ArrayList<Token> scan() {
        while (!isAtEnd()) {
            char current = advance();
            switch (current) {
                case '?': addToken(TokenType.TERNARY, "?"); break;
                case '+': {
                    if (match('=')) {
                        addToken(TokenType.ASSIGN_ADD, "+=");
                    } else {
                        addToken(TokenType.OPERATOR, "+");
                    }
                    break;
                }
                case '-': {
                    if (match('=')) {
                        addToken(TokenType.ASSIGN_SUB, "-=");
                        break;
                    } else if (match('>')) {
                        addToken(TokenType.GO, "->");
                        break;
                    } else if (Character.isDigit(peek())) {
                        tokens.add(scanNumber('-'));
                        break;
                    } else {
                        addToken(TokenType.OPERATOR, "-");
                        break;
                    }
                }
                case '*': {
                    if (match('=')) {
                        addToken(TokenType.ASSIGN_MUL, "*=");
                    } else {
                        addToken(TokenType.OPERATOR, "*");
                    }
                    break;
                }
                case '%': addToken(TokenType.OPERATOR, "%"); break;
                case '#': {
                    while (!isAtEnd() && !match('#')) advance();
                    break;
                }
                case '/': {
                    if (match('=')) {
                        addToken(TokenType.ASSIGN_DIVIDE, "/=");
                        break;
                    } else if (match('/')) {
                        while (!match('\n') && !isAtEnd()) {
                            advance();
                        }
                        line += 1;
                        break;
                    } else if (match('*')) {
                        while(!(peek() == '*' && next() == '/') && !isAtEnd()) {
                            if (match('\n')) {
                                line += 1;
                                continue;
                            }
                            advance();
                        }
                        // пропускаем *
                        advance();
                        // пропускаем /
                        advance();
                        break;
                    } else {
                        addToken(TokenType.OPERATOR, "/");
                        break;
                    }
                }
                case '(': addToken(TokenType.LEFT_PAREN, "("); break;
                case ')': addToken(TokenType.RIGHT_PAREN, ")"); break;
                case '[': addToken(TokenType.LEFT_BRACKET, "["); break;
                case ']': addToken(TokenType.RIGHT_BRACKET, "]"); break;
                case '{': addToken(TokenType.LEFT_BRACE, "{"); break;
                case '}': addToken(TokenType.RIGHT_BRACE, "}"); break;
                case ',': addToken(TokenType.COMMA, ","); break;
                case '.': addToken(TokenType.DOT, "."); break;
                case ':': {
                    if (match('=')) {
                        addToken(TokenType.WALRUS, ":=");
                    } else {
                        addToken(TokenType.COLON, ":");
                    }
                    break;
                }
                case '<': {
                    if (match('=')) {
                        addToken(TokenType.LOWER_EQUAL, "<=");
                    } else {
                        addToken(TokenType.LOWER, "<");
                    }
                    break;
                }
                case '>': {
                    if (match('=')) {
                        addToken(TokenType.BIGGER_EQUAL, ">=");
                    } else {
                        addToken(TokenType.BIGGER, ">");
                    }
                    break;
                }
                case '!': {
                    if (match('=')) {
                        addToken(TokenType.NOT_EQUAL, "!=");
                    } else {
                        throw new WattParsingError(
                                line,
                                filename,
                                "Bang operator is not available now.",
                                "You can use != operator instead!"
                        );
                    }
                    break;
                }
                case '=': {
                    if (match('=')) {
                        addToken(TokenType.EQUAL, "==");
                    } else {
                        addToken(TokenType.ASSIGN, "=");
                    }
                    break;
                }
                // игнорируем пробелы, переносы, на новые строки переход и тд
                case ' ': {
                    break;
                }
                case '\r': {
                    break;
                }
                case '\t': {
                    break;
                }
                case '\n': {
                    line++;
                    break;
                }
                case '\'': {
                    addToken(TokenType.TEXT, scanString());
                    break;
                }
                default: {
                    if (Character.isDigit(current)) {
                        tokens.add(scanNumber(current));
                    } else if (Character.isLetter(current) || current == '_') {
                        tokens.add(scanIdentifierOrKeyword(current));
                    } else {
                        throw new WattParsingError(
                                line,
                                filename,
                                "Unexpected character: " + current,
                                "Check your code for character: " + current);
                    }
                }
            }
        }

        // возвращаем токены
        return tokens;
    }

    // сканируем строку
    private String scanString() {
        StringBuilder text = new StringBuilder();
        while (peek() != '\'') {
            if (match('\n')) {
                line += 1;
                throw new WattParsingError(
                        line,
                        filename,
                        "Unclosed string quotes: " + text.substring(0, Math.min(text.length(), 15)),
                        "Did you forgot to type \" symbol?");
            }
            if (isAtEnd()) {
                throw new WattParsingError(
                        line,
                        filename,
                        "Unclosed string quotes: " + text.substring(0, Math.min(text.length(), 15)),
                        "Did you forgot to type \" symbol?");
            }
            text.append(advance());
        }
        if (isAtEnd()) {
            throw new WattParsingError(
                    line,
                    filename,
                    "Unclosed string quotes: " + text.substring(0, Math.min(text.length(), 15)),
                    "Did you forgot to type \" symbol?");
        }
        advance();
        return text.toString();
    }

    // сканируем число
    private Token scanNumber(char start) {
        StringBuilder text = new StringBuilder(String.valueOf(start));
        while (Character.isDigit(peek()) || peek() == '.') {
            if (match('\n')) {
                line += 1;
                continue;
            }
            text.append(advance());
            if (isAtEnd()) {
                break;
            }
        }
        return new Token(TokenType.NUM, text.toString(), line, filename);
    }

    // сканируем идентификатор или ключевое слово
    private Token scanIdentifierOrKeyword(char start) {
        StringBuilder text = new StringBuilder(String.valueOf(start));
        while (Character.isLetter(peek()) || Character.isDigit(peek()) || peek() == '_' || peek() == '-') {
            if (match('\n')) {
                line += 1;
                continue;
            }
            text.append(advance());
            if (isAtEnd()) {
                break;
            }
        }
        TokenType type = keywords.getOrDefault(text.toString(), TokenType.ID);
        return new Token(type, text.toString(), line, filename);
    }

    // в конце ли
    private boolean isAtEnd() {
        return current >= code.length();
    }

    // следующий символ
    private char advance() {
        return code.charAt(current++);
    }

    // символ на текущей позиции без добавления
    // еденицы к текущему символу
    private char peek() {
        if (isAtEnd()) return '\1';
        return code.charAt(current);
    }

    // символ на следующей позиции без добавления
    // еденицы к текущему символу
    private char next() {
        if (current+1 >= code.length()) return '\1';
        return code.charAt(current+1);
    }

    // добавление токена
    private void addToken(TokenType type, String value) {
        tokens.add(new Token(type, value, line, filename));
    }

    // проверка на текущий
    private boolean match(char character) {
        if (isAtEnd()) return false;
        if (code.charAt(current) == character) {
            current += 1;
            return true;
        } else {
            return false;
        }
    }
}
