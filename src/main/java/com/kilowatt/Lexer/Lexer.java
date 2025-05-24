package com.kilowatt.Lexer;

import com.kilowatt.Errors.WattParseError;
import com.kilowatt.WattVM.VmAddress;
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
    private int line = 0;
    private int current = 0;
    private int column = 0;
    private String lineText = "";
    // кейворды
    private static final Map<String, TokenType> keywords = new HashMap<>() {{
        put("fun", TokenType.FUN);
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
        put("default", TokenType.DEFAULT);
        put("for", TokenType.FOR);
        put("continue", TokenType.CONTINUE);
        put("try", TokenType.TRY);
        put("catch", TokenType.CATCH);
        put("throw", TokenType.THROW);
        put("in", TokenType.IN);
        put("unit", TokenType.UNIT);
        put("native", TokenType.NATIVE);
        put("lambda", TokenType.LAMBDA);
        put("with", TokenType.WITH);
        put("trait", TokenType.TRAIT);
        put("impl", TokenType.IMPL);
        put("impls", TokenType.IMPLS);
    }};

    // сканнер
    public Lexer(String filename, String code)
    {
        this.filename = filename;
        this.code = code;
    }

    // скан кода
    public ArrayList<Token> scan() {
        // первая строка
        newLine();
        // лексинг
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
                        addToken(TokenType.ARROW, "->");
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
                case '/': {
                    if (match('=')) {
                        addToken(TokenType.ASSIGN_DIVIDE, "/=");
                        break;
                    } else if (match('/')) {
                        while (!match('\n') && !isAtEnd()) {
                            advance();
                        }
                        newLine();
                        break;
                    } else if (match('*')) {
                        while(!(peek() == '*' && next() == '/') && !isAtEnd()) {
                            if (match('\n')) {
                                newLine();
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
                case '(': addToken(TokenType.LPAREN, "("); break;
                case ')': addToken(TokenType.RPAREN, ")"); break;
                case '[': addToken(TokenType.LBRACKET, "["); break;
                case ']': addToken(TokenType.RBRACKET, "]"); break;
                case '{': addToken(TokenType.LBRACE, "{"); break;
                case '}': addToken(TokenType.RBRACE, "}"); break;
                case ',': addToken(TokenType.COMMA, ","); break;
                case '.': {
                    if (match('.')) {
                        addToken(TokenType.RANGE, "..");
                    } else {
                        addToken(TokenType.DOT, ".");
                    }
                    break;
                }
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
                        addToken(TokenType.LESS_EQ, "<=");
                    } else {
                        addToken(TokenType.LESS, "<");
                    }
                    break;
                }
                case '>': {
                    if (match('=')) {
                        addToken(TokenType.GREATER_EQ, ">=");
                    } else {
                        addToken(TokenType.GREATER, ">");
                    }
                    break;
                }
                case '!': {
                    if (match('=')) {
                        addToken(TokenType.NOT_EQUAL, "!=");
                    } else {
                        addToken(TokenType.OPERATOR, "!");
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
                    newLine();
                    break;
                }
                case '\'': {
                    addToken(TokenType.TEXT, scanString());
                    break;
                }
                case '|': {
                    if (match('>')) {
                        addToken(TokenType.PIPE, "|>");
                    } else {
                        throw new WattParseError(
                            new VmAddress(filename, line, column, lineText),
                            "expected > after | for pipe!",
                            "check your code."
                        );
                    }
                    break;
                }
                default: {
                    if (isDigit(current)) {
                        tokens.add(scanNumber(current));
                    } else if (isId(current)) {
                        tokens.add(scanIdentifierOrKeyword(current));
                    } else {
                        throw new WattParseError(
                            new VmAddress(filename, line, column, lineText),
                            "unexpected char: " + current,
                            "delete char: " + current);
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
                throw new WattParseError(
                    new VmAddress(filename, line, column, lineText),
                    "unclosed string quotes: " + text.substring(0, Math.min(text.length(), 15)),
                    "did you forget to type ' symbol?");
            }
            if (isAtEnd()) {
                throw new WattParseError(
                    new VmAddress(filename, line, column, lineText),
                    "unclosed string quotes: " + text.substring(0, Math.min(text.length(), 15)),
                    "did you forget to type ' symbol?");
            }
            text.append(advance());
        }
        advance();
        return text.toString();
    }

    // сканируем число
    private Token scanNumber(char start) {
        StringBuilder text = new StringBuilder(String.valueOf(start));
        boolean isFloat = false;
        while (isDigit(peek()) || peek() == '.') {
            if (peek() == '.') {
                if (next() == '.') break;
                if (isFloat) {
                    throw new WattParseError(
                        new VmAddress(filename, line, column, lineText),
                        "double dot number: " + text + ".",
                        "check your code."
                    );
                }
                isFloat = true;
            }
            text.append(advance());
            if (isAtEnd()) {
                break;
            }
        }
        return new Token(TokenType.NUM, text.toString(), line, column, filename, lineText);
    }

    // сканируем идентификатор или ключевое слово
    private Token scanIdentifierOrKeyword(char start) {
        StringBuilder text = new StringBuilder(String.valueOf(start));
        while (isId(peek())) {
            if (match('\n')) {
                newLine();
                continue;
            }
            text.append(advance());
            if (isAtEnd()) {
                break;
            }
        }
        TokenType type = keywords.getOrDefault(text.toString(), TokenType.ID);
        return new Token(type, text.toString(), line, column, filename, lineText);
    }

    // скип новой строки
    private void newLine() {
        line++;
        column = 0;
        lineText = getLineText();
    }

    // получение текста строки
    private String getLineText() {
        int i = 0;
        StringBuilder text = new StringBuilder();
        while (!(current + i >= code.length()) && peek(i) != '\n') {
            text.append(peek(i));
            i += 1;
        }
        return text.toString();
    }

    // в конце ли
    private boolean isAtEnd() {
        return current >= code.length();
    }

    // следующий символ
    private char advance() {
        char ch = code.charAt(current++);
        column++;
        return ch;
    }

    // символ на текущей позиции без добавления
    // еденицы к текущему символу
    private char peek() {
        if (isAtEnd()) return '\1';
        return code.charAt(current);
    }

    // символ на текущей позиции + оффсет
    private char peek(int offset) {
        if (isAtEnd()) return '\1';
        return code.charAt(current + offset);
    }

    // символ на следующей позиции без добавления
    // еденицы к текущему символу
    private char next() {
        if (current+1 >= code.length()) return '\1';
        return code.charAt(current+1);
    }

    // добавление токена
    private void addToken(TokenType type, String value) {
        tokens.add(new Token(type, value, line, column, filename, lineText));
    }

    // проверка на текущий
    private boolean match(char character) {
        if (isAtEnd()) return false;
        if (code.charAt(current) == character) {
            current += 1;
            column++;
            return true;
        } else {
            return false;
        }
    }

    // цифра ли
    private boolean isDigit(char character) {
        return character >= '0' && character <= '9';
    }

    // буква ли
    private boolean isAlpha(char character) {
        return (character >= 'a' && character <= 'z') ||
            (character >= 'A' && character <= 'Z') ||
            (character == '_');
    }

    // подходит ли для ID
    private boolean isId(char character) {
        return isAlpha(character) ||
            isDigit(character) ||
            (character == ':' && isId(next()));
    }
}
