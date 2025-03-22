package com.kilowatt.Parser;

import com.kilowatt.Errors.WattParsingError;
import com.kilowatt.Lexer.Token;
import com.kilowatt.Lexer.TokenType;
import com.kilowatt.Parser.AST.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
Парсер
 */
@SuppressWarnings({"BooleanMethodIsAlwaysInverted"})
@Getter
public class Parser {
    private final String filename;
    private final ArrayList<Token> tokenList;
    private int current = 0;

    public Parser(String filename, ArrayList<Token> tokenList) {
        this.filename = filename;
        this.tokenList = tokenList;
    }

    public BlockNode parse() {
        return block();
    }

    // аргументы
    private ArrayList<Node> args() {
        ArrayList<Node> nodes = new ArrayList<>();
        consume(TokenType.LEFT_PAREN);

        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                if (check(TokenType.COMMA)) {
                    consume(TokenType.COMMA);
                }
                nodes.add(expression());
            } while (!isAtEnd() && check(TokenType.COMMA));
        }

        consume(TokenType.RIGHT_PAREN);
        return nodes;
    }

    // параметры
    private ArrayList<Token> params() {
        ArrayList<Token> params = new ArrayList<>();
        consume(TokenType.LEFT_PAREN);

        if (check(TokenType.ID)) {
            do {
                if (check(TokenType.COMMA)) {
                    consume(TokenType.COMMA);
                }
                params.add(consume(TokenType.ID));
            } while (!isAtEnd() && check(TokenType.COMMA));
        }

        consume(TokenType.RIGHT_PAREN);
        return params;
    }

    // часть цепочки доступа
    private AccessNode accessPart(AccessNode prev) {
        // выражение с идентификатором
        if (check(TokenType.ID)) {
            Token identifier = consume(TokenType.ID);
            if (check(TokenType.WALRUS)) {
                consume(TokenType.WALRUS);
                return new VarDefineNode(prev, identifier, expression());
            } else if (check(TokenType.ASSIGN)) {
                consume(TokenType.ASSIGN);
                return new VarSetNode(prev, identifier, expression());
            } else if (check(TokenType.ASSIGN_ADD) ||
                check(TokenType.ASSIGN_SUB) ||
                check(TokenType.ASSIGN_MUL) ||
                check(TokenType.ASSIGN_DIVIDE)
            ) {
                // оператор и локация
                String op = null;
                Token location = null;
                // ищем оператор
                switch (peek().type) {
                    case TokenType.ASSIGN_SUB -> {
                        location = consume(TokenType.ASSIGN_SUB);
                        op = "-";
                    }
                    case TokenType.ASSIGN_ADD -> {
                        location = consume(TokenType.ASSIGN_ADD);
                        op = "+";
                    }
                    case TokenType.ASSIGN_MUL -> {
                        location = consume(TokenType.ASSIGN_MUL);
                        op = "*";
                    }
                    case TokenType.ASSIGN_DIVIDE -> {
                        location = consume(TokenType.ASSIGN_DIVIDE);
                        op = "/";
                    }
                    default -> {
                        throw new WattParsingError(
                                identifier.getLine(),
                                identifier.getFileName(),
                                "operator not found: " + peek(),
                                "check your code."
                        );
                    }
                }
                // возвращаем
                VarNode var = new VarNode(prev, identifier);
                var.setShouldPushResult(true);
                return new VarSetNode(prev, identifier, new BinNode(
                        var, expression(), new Token(TokenType.OPERATOR, op,
                        location.getLine(), location.getFileName())
                ));
            } else if (check(TokenType.ASSIGN_DIVIDE)) {
                Token location = consume(TokenType.ASSIGN_DIVIDE);
                return new VarSetNode(prev, identifier, new BinNode(
                        prev, expression(), new Token(TokenType.OPERATOR, "/",
                        location.getLine(), location.getFileName())
                ));
            } else if (check(TokenType.ASSIGN_MUL)) {
                Token location = consume(TokenType.ASSIGN_MUL);
                return new VarSetNode(prev, identifier, new BinNode(
                        prev, expression(), new Token(TokenType.OPERATOR, "*",
                        location.getLine(), location.getFileName())
                ));
            } else if (check(TokenType.ASSIGN_SUB)) {
                Token location = consume(TokenType.ASSIGN_SUB);
                return new VarSetNode(prev, identifier, new BinNode(
                        prev, expression(), new Token(TokenType.OPERATOR, "-",
                        location.getLine(), location.getFileName())
                ));
            } else if (check(TokenType.LEFT_PAREN)) {
                return new CallNode(prev, identifier, args());
            } else {
                return new VarNode(prev, identifier);
            }
        }
        // выражение со словом new
        else {
            return objectCreation();
        }
    }

    // доступ (вызов, получение значения переменной,
    // установка значения переменной) в виде выражения.
    private AccessNode accessExpr() {
        AccessNode left = accessPart(null);

        while (check(TokenType.DOT)) {
            consume(TokenType.DOT);
            left.setShouldPushResult(true);
            left = accessPart(left);
            if (left instanceof VarDefineNode defineNode) {
                throw new WattParsingError(
                        defineNode.getName().line,
                        filename,
                        "couldn't use define in expr.",
                        "check your code.");
            }
            else if (left instanceof VarSetNode defineNode) {
                throw new WattParsingError(
                        defineNode.getName().line,
                        filename,
                        "couldn't use assign in expr.",
                        "check your code.");
            }
        }

        left.setShouldPushResult(true);
        return left;
    }


    // доступ (вызов, получение значения переменной,
    // установка значения переменной) в виде инструкции.
    private AccessNode accessStatement() {
        AccessNode left = accessPart(null);

        while (check(TokenType.DOT)) {
            consume(TokenType.DOT);
            left.setShouldPushResult(true);
            left = accessPart(left);
            if (left instanceof VarDefineNode ||
                    left instanceof VarSetNode) {
                break;
            }
        }

        left.setShouldPushResult(false);
        return left;
    }

    // выражение в скобках
    private Node grouping() {
        consume(TokenType.LEFT_PAREN);
        Node expr = expression();
        consume(TokenType.RIGHT_PAREN);
        return expr;
    }

    // анонимная функция
    private Node anonFunction() {
        Token location = consume(TokenType.FUN);
        ArrayList<Token> parameters = params();
        consume(TokenType.LEFT_BRACE);
        BlockNode node = block();
        consume(TokenType.RIGHT_BRACE);
        return new AnonymousFnNode(
                location,
                node,
                parameters
        );
    }

    // лямбда функия
    private Node lambdaFunction() {
        Token location = consume(TokenType.LAMBDA);
        ArrayList<Token> parameters = params();
        consume(TokenType.GO);
        BlockNode node = BlockNode.of(
                new ReturnNode(
                        location,
                        expression()
                )
        );
        return new AnonymousFnNode(
                location,
                node,
                parameters
        );
    }

    // пайп-оператор
    private AccessNode pipeOperator(AccessNode _expr) {
        AccessNode expr = _expr;
        if (expr instanceof AccessNode accessExpr) {
            accessExpr.setShouldPushResult(true);
        } else {
            throw new WattParsingError(
                    peek().getLine(),
                    peek().getFileName(),
                    "expected call expr in pipes.",
                    "check your code."
            );
        }
        if (check(TokenType.PIPE)) {
            while (check(TokenType.PIPE)) {
                Token location = consume(TokenType.PIPE);
                if (expr instanceof CallNode) {
                    if (expression() instanceof CallNode nextCallNode) {
                        nextCallNode.getArgs().addFirst(expr);
                        expr = nextCallNode;
                    } else {
                        throw new WattParsingError(
                                location.getLine(),
                                location.getFileName(),
                                "expected call expr in pipes.",
                                "check your code."
                        );
                    }
                } else {
                    throw new WattParsingError(
                            location.getLine(),
                            location.getFileName(),
                            "expected call expr in pipes.",
                            "check your code."
                    );
                }
            }
        }
        return expr;
    }

    // примарное выражение (базовое)
    private Node primary() {
        switch (peek().type) {
            case TokenType.ID, TokenType.NEW -> {
                AccessNode access = accessExpr();
                if (check(TokenType.PIPE)) {
                    AccessNode pipe = pipeOperator(access);
                    pipe.setShouldPushResult(true);
                    return pipe;
                } else {
                    return access;
                }
            }
            case TokenType.NUM -> {
                return new NumberNode(consume(TokenType.NUM));
            }
            case TokenType.TEXT -> {
                return new StringNode(consume(TokenType.TEXT));
            }
            case TokenType.BOOL -> {
                return new BoolNode(consume(TokenType.BOOL));
            }
            case TokenType.LEFT_PAREN -> {
                return grouping();
            }
            case TokenType.LEFT_BRACKET -> {
                return listNode();
            }
            case TokenType.LEFT_BRACE -> {
                return mapNode();
            }
            case TokenType.NULL -> {
                return nullNode();
            }
            case TokenType.FUN -> {
                return anonFunction();
            }
            case TokenType.LAMBDA -> {
                return lambdaFunction();
            }
            case TokenType.MATCH -> {
                return matchExpr();
            }
            default -> throw new WattParsingError(
                     peek().line,
                     filename,
                     "invalid token in primary parsing: " + peek().type + "::" + peek().value,
                    "did you write wrong expr?");
        }
    }

    // нулл нода
    private Node nullNode() {
        Token location = consume(TokenType.NULL);
        return new NullNode(location);
    }

    // список нод
    private Node listNode() {
        Token location = consume(TokenType.LEFT_BRACKET);
        ArrayList<Node> nodes = new ArrayList<>();

        if (check(TokenType.RIGHT_BRACKET)) {
            consume(TokenType.RIGHT_BRACKET);
            return new ListNode(location, nodes);
        }

        do {
            if (check(TokenType.COMMA)) {
                consume(TokenType.COMMA);
            }
            nodes.add(expression());
        }
        while (check(TokenType.COMMA));

        consume(TokenType.RIGHT_BRACKET);
        return new ListNode(location, nodes);
    }

    // словарь нод
    private Node mapNode() {
        Token location = consume(TokenType.LEFT_BRACE);
        HashMap<Node, Node> nodes = new HashMap<>();

        if (check(TokenType.RIGHT_BRACE)) {
            consume(TokenType.RIGHT_BRACE);
            return new MapNode(location, nodes);
        }

        do {
            if (check(TokenType.COMMA)) {
                consume(TokenType.COMMA);
            }
            Node key = expression();
            consume(TokenType.COLON);
            Node value = expression();
            nodes.put(key, value);
        }
        while (check(TokenType.COMMA));

        consume(TokenType.RIGHT_BRACE);
        return new MapNode(location, nodes);
    }

    // умножение, деление
    private Node multiplicative() {
        Node left = primary();

        while(check(TokenType.OPERATOR) && (match("*") || match("/"))) {
            Token operator = consume(TokenType.OPERATOR);
            Node right = primary();
            left = new BinNode(left, right, operator);
        }

        return left;
    }

    // сложение, вычитание
    private Node additive() {
        Node left = multiplicative();

        while(check(TokenType.OPERATOR) && (match("+") || match("-"))) {
            Token operator = consume(TokenType.OPERATOR);
            Node right = multiplicative();
            left = new BinNode(left, right, operator);
        }

        return left;
    }

    // условный оператор
    private Token conditionalOperator() {
        return switch (peek().type) {
            case EQUAL -> consume(TokenType.EQUAL);
            case NOT_EQUAL -> consume(TokenType.NOT_EQUAL);
            case LOWER -> consume(TokenType.LOWER);
            case BIGGER -> consume(TokenType.BIGGER);
            case LOWER_EQUAL -> consume(TokenType.LOWER_EQUAL);
            case BIGGER_EQUAL -> consume(TokenType.BIGGER_EQUAL);
            default -> throw new WattParsingError(
                    peek().line,
                    filename,
                    "invalid cond. op: " + peek().value,
                    "available op-s: ==,!=,>,<,>=,<=");
        };
    }

    // условное выражение
    private Node conditional() {
        Node left = additive();

        if (check(TokenType.BIGGER) || check(TokenType.LOWER) || check(TokenType.BIGGER_EQUAL) ||
            check(TokenType.LOWER_EQUAL) || check(TokenType.EQUAL) || check(TokenType.NOT_EQUAL)) {
            Token operator = conditionalOperator();
            Node right = additive();
            return new ConditionalNode(left, right, operator);
        }

        return left;
    }

    // логическое выражение
    private Node logical() {
        Node left = conditional();

        while(check(TokenType.AND) || check(TokenType.OR)) {
            Token operator;
            if (check(TokenType.AND)) {
                operator = consume(TokenType.AND);
            } else {
                operator = consume(TokenType.OR);
            }
            Node right = conditional();
            left = new LogicalNode(left, right, operator);
        }

        return left;
    }

    // текущий токен это закрывающая скобка?
    private boolean itsClosingBrace() {
        return check(TokenType.RIGHT_BRACE);
    }

    // блок кода
    private BlockNode block() {
        ArrayList<Node> nodes = new ArrayList<>();

        while (!isAtEnd() && !itsClosingBrace()) {
            nodes.add(statement());
        }

        return new BlockNode(nodes);
    }

    // нативная функция
    private Node nativeFunction() {
        consume(TokenType.NATIVE);
        Token name = consume(TokenType.ID);
        consume(TokenType.GO);
        Token javaName = consume(TokenType.TEXT);
        return new NativeNode(name,javaName);
    }

    // функция
    private Node function() {
        // адресс и имя
        Token address = consume(TokenType.FUN);
        Token name = consume(TokenType.ID);
        // параметры
        ArrayList<Token> parameters = params();
        consume(TokenType.GO);
        // тело
        consume(TokenType.LEFT_BRACE);
        BlockNode node = block();
        // добавляем возврат null
        node.getNodes().add(new ReturnNode(
                address, new NullNode(address))
        );
        consume(TokenType.RIGHT_BRACE);
        // возвращаем функцию
        return new FnNode(node,name,parameters);
    }

    // объявление типа
    private Node type() {
        consume(TokenType.TYPE);
        Token name = consume(TokenType.ID);
        ArrayList<Token> constructor = new ArrayList<>();
        if (check(TokenType.LEFT_PAREN)) {
            constructor = params();
        }
        consume(TokenType.GO);
        consume(TokenType.LEFT_BRACE);
        ArrayList<Node> nodes = new ArrayList<>();
        while (!isAtEnd() && !itsClosingBrace()) {
            Node node = statement();
            if (node instanceof FnNode || node instanceof NativeNode || node instanceof VarDefineNode ||
                node instanceof VarSetNode) {
                nodes.add(node);
            } else {
                throw new WattParsingError(
                        peek().line,
                        filename,
                        "invalid node token for type: " + peek().type + ":" + peek().value,
                        "available: fun, variable definition; variable set.");
            }
        }
        consume(TokenType.RIGHT_BRACE);
        return new TypeNode(name, nodes, constructor);
    }

    // объявление юнита
    private Node unit() {
        consume(TokenType.UNIT);
        Token name = consume(TokenType.ID);
        consume(TokenType.GO);
        consume(TokenType.LEFT_BRACE);
        ArrayList<Node> nodes = new ArrayList<>();
        while (!isAtEnd() && !itsClosingBrace()) {
            Node node = statement();
            if (node instanceof FnNode || node instanceof NativeNode || node instanceof VarDefineNode ||
                    node instanceof VarSetNode) {
                nodes.add(node);
            } else {
                throw new WattParsingError(
                        peek().line,
                        filename,
                        "invalid node token for unit: " + peek().type + "::" + peek().value,
                        "available: fun, variable definition; variable set.");
            }
        }
        consume(TokenType.RIGHT_BRACE);
        return new UnitNode(name, nodes);
    }

    // выражение (рекурсивный парсинг)
    private Node expression() {
        return logical();
    }

    // выражение создания объекта
    private AccessNode objectCreation() {
        consume(TokenType.NEW);
        Token name = consume(TokenType.ID);
        return new NewInstanceNode(name, args());
    }

    // инструкция
    private Node statement() {
        switch (peek().type) {
            case TokenType.ID, TokenType.NEW -> {
                AccessNode access = accessStatement();
                if (check(TokenType.PIPE)) {
                    AccessNode pipe = pipeOperator(access);
                    pipe.setShouldPushResult(false);
                    return pipe;
                } else {
                    return access;
                }
            }
            case TokenType.FUN -> {
                return function();
            }
            case TokenType.NATIVE -> {
                return nativeFunction();
            }
            case TokenType.TYPE -> {
                return type();
            }
            case TokenType.UNIT -> {
                return unit();
            }
            case TokenType.WHILE -> {
                return whileLoop();
            }
            case TokenType.FOR -> {
                return forLoop();
            }
            case TokenType.IF -> {
                return ifNode();
            }
            case TokenType.IMPORT -> {
                return importNode();
            }
            case TokenType.RETURN -> {
                return returnNode();
            }
            case TokenType.MATCH -> {
                return matchStmt();
            }
            case TokenType.CONTINUE -> {
                return continueNode();
            }
            case TokenType.BREAK -> {
                return breakNode();
            }
            case TokenType.ASSERT -> {
                return assertion();
            }
            default -> throw new WattParsingError(
                    peek().line,
                    filename,
                    "unexpected stmt token: " + peek(),
                    "check your code.");
        }
    }

    // стэйтмент continue
    private Node continueNode() {
        Token loc = consume(TokenType.CONTINUE);
        return new ContinueNode(loc);
    }

    // стэйтмент break
    private Node breakNode() {
        Token loc = consume(TokenType.BREAK);
        return new BreakNode(loc);
    }

    // стэйтмент return
    private Node returnNode() {
        Token loc = consume(TokenType.RETURN);
        return new ReturnNode(loc, expression());
    }

    // стэйтмент assert
    private Node assertion() {
        Token loc = consume(TokenType.ASSERT);
        return new AssertNode(loc, expression());
    }

    // стэйтмент import
    private Node importNode() {
        consume(TokenType.IMPORT);
        ArrayList<Token> imports = new ArrayList<>();
        if (check(TokenType.LEFT_PAREN)) {
            consume(TokenType.LEFT_PAREN);
            do {
                if (check(TokenType.COMMA)) {
                    consume(TokenType.COMMA);
                }
                imports.add(consume(TokenType.TEXT));
            }
            while (check(TokenType.COMMA));

            consume(TokenType.RIGHT_PAREN);
        } else {
            imports.add(consume(TokenType.TEXT));
        }
        return new ImportNode(imports);
    }

    // стэйтмент while
    private Node whileLoop() {
        Token location = consume(TokenType.WHILE);
        Node expr = expression();
        consume(TokenType.LEFT_BRACE);
        BlockNode node = block();
        consume(TokenType.RIGHT_BRACE);
        return new WhileNode(location, node, expr);
    }

    // стэйтмент else
    private IfNode elseNode() {
        Token elseTok = consume(TokenType.ELSE);
        consume(TokenType.LEFT_BRACE);
        BlockNode node = block();
        consume(TokenType.RIGHT_BRACE);
        return new IfNode(elseTok, node, new BoolNode(new Token(TokenType.BOOL, "true", elseTok.line, filename)), null);
    }

    // стэйтмент elif
    private IfNode elifNode() {
        Token location = consume(TokenType.ELIF);
        Node logical = expression();
        consume(TokenType.LEFT_BRACE);
        BlockNode node = block();
        consume(TokenType.RIGHT_BRACE);
        IfNode ifNode = new IfNode(location, node, logical, null);
        // else
        if (check(TokenType.ELIF)) {
            ifNode.setElseNode(elifNode());
        } else if (check(TokenType.ELSE)) {
            ifNode.setElseNode(elseNode());
        }
        // возвращаем
        return ifNode;
    }

    // стэйтмент if
    private Node ifNode() {
        Token location = consume(TokenType.IF);
        Node logical = expression();
        consume(TokenType.LEFT_BRACE);
        BlockNode node = block();
        consume(TokenType.RIGHT_BRACE);
        IfNode ifNode = new IfNode(location, node, logical, null);
        // else
        if (check(TokenType.ELIF)) {
            ifNode.setElseNode(elifNode());
        } else if (check(TokenType.ELSE)) {
            ifNode.setElseNode(elseNode());
        }
        // возвращаем
        return ifNode;
    }

    // стэйтмент for
    private Node forLoop() {
        consume(TokenType.FOR);
        Token name = consume(TokenType.ID);
        consume(TokenType.IN);
        Node from = expression();
        boolean isDecrement = false;
        if (check(TokenType.TO)) {
            consume(TokenType.TO);
        } else if (check(TokenType.FROM)) {
            consume(TokenType.FROM);
            isDecrement = true;
        }
        Node to = expression();
        consume(TokenType.LEFT_BRACE);
        BlockNode node = block();
        consume(TokenType.RIGHT_BRACE);
        return new ForNode(node, name, new RangeNode(from, to, isDecrement));
    }

    // выражение match
    private Node matchExpr() {
        Token location = consume(TokenType.MATCH);
        Node matchable = expression();
        List<MatchNode.Case> cases = new ArrayList<>();
        MatchNode.Case defaultCase;
        consume(TokenType.LEFT_BRACE);
        while (check(TokenType.CASE)) {
            consume(TokenType.CASE);
            Node equality = expression();
            consume(TokenType.GO);
            cases.add(
                    new MatchNode.Case(
                            equality,
                            expression()
                    )
            );
        }
        if (check(TokenType.DEFAULT)) {
            consume(TokenType.DEFAULT);
            consume(TokenType.GO);
            defaultCase = new MatchNode.Case(null, expression());
        } else {
            Token token = tokenList.get(current);
            throw new WattParsingError(
                token.getLine(),
                token.getFileName(),
                "couldn't use match expr without default case.",
                "check your code."
            );
        }
        consume(TokenType.RIGHT_BRACE);
        return new MatchNode(
                location,
                matchable,
                cases,
                defaultCase
        );
    }

    // стэйтмент match
    private Node matchStmt() {
        Token location = consume(TokenType.MATCH);
        Node matchable = expression();
        List<MatchNode.Case> cases = new ArrayList<>();
        MatchNode.Case defaultCase = null;
        consume(TokenType.LEFT_BRACE);
        while (check(TokenType.CASE)) {
            consume(TokenType.CASE);
            Node equality = expression();
            if (check(TokenType.GO)) {
                consume(TokenType.GO);
                cases.add(new MatchNode.Case(equality, statement()));
            } else {
                consume(TokenType.LEFT_BRACE);
                cases.add(new MatchNode.Case(equality, block()));
                consume(TokenType.RIGHT_BRACE);
            }
        }
        if (check(TokenType.DEFAULT)) {
            consume(TokenType.DEFAULT);
            if (check(TokenType.GO)) {
                defaultCase = new MatchNode.Case(null, statement());
            } else {
                consume(TokenType.LEFT_BRACE);
                defaultCase = new MatchNode.Case(null, block());
                consume(TokenType.RIGHT_BRACE);
            }
        }
        consume(TokenType.RIGHT_BRACE);
        return new MatchNode(
                location,
                matchable,
                cases,
                defaultCase
        );
    }

    // в конце ли?
    private boolean isAtEnd() {
        return current >= tokenList.size();
    }

    // проверка на совпадение типа токена
    private boolean check(TokenType expected) {
        if (isAtEnd()) { return false; }
        return this.tokenList.get(current).type == expected;
    }

    // проверка на совпадение значения токена
    private boolean match(String value) {
        if (isAtEnd()) { return false; }
        return this.tokenList.get(current).value.equals(value);
    }

    // "съедание" токена
    private Token consume(TokenType expected) {
        if (isAtEnd()) {
            Token token = this.tokenList.get(current-1);
            throw new WattParsingError(
                token.line,
                filename,
                "couldn't consume token.",
                "its end of file! last token: " + token.type + ":" + token.value);
        }
        Token token = this.tokenList.get(current);
        if (token.type == expected) {
            current += 1;
            return token;
        } else {
            throw new WattParsingError(
                    token.line,
                    filename,
                    "unexpected token: " + token.type + ":" + token.value,
                    "did you mean " + expected + "?");
        }
    }

    // получаем текущий токен
    private Token peek() {
        if (isAtEnd()) {
            Token token = this.tokenList.get(current-1);
            throw new WattParsingError(
                    token.line,
                    filename,
                    "couldn't peek token.",
                    "its end of file! last token: " + token.type + ":" + token.value);
        } else {
            return this.tokenList.get(current);
        }
    }
}
