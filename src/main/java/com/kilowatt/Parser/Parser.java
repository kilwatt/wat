package com.kilowatt.Parser;

import com.kilowatt.Errors.WattParsingError;
import com.kilowatt.Lexer.Token;
import com.kilowatt.Lexer.TokenType;
import com.kilowatt.Parser.AST.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
Парсер
 */
@SuppressWarnings({"BooleanMethodIsAlwaysInverted"})
@Getter
public class Parser {
    // имя файла
    private final String filename;
    // токены
    private final ArrayList<Token> tokenList;
    // префикс полного имени
    @Setter
    private String fullNamePrefix;
    // текущий иднекс
    private int current = 0;

    // конструктор
    public Parser(String filename, ArrayList<Token> tokenList) {
        this.filename = filename;
        this.fullNamePrefix = filename.replace(".wt", "");
        this.tokenList = tokenList;
    }

    // парсинг
    public BlockNode parse() {
        return block();
    }

    // аргументы
    private ArrayList<Node> args() {
        ArrayList<Node> nodes = new ArrayList<>();
        consume(TokenType.LPAREN);

        if (!check(TokenType.RPAREN)) {
            do {
                if (check(TokenType.COMMA)) {
                    consume(TokenType.COMMA);
                }
                nodes.add(expression());
            } while (!isAtEnd() && check(TokenType.COMMA));
        }

        consume(TokenType.RPAREN);
        return nodes;
    }

    // параметры
    private ArrayList<Token> params() {
        ArrayList<Token> params = new ArrayList<>();
        consume(TokenType.LPAREN);

        if (check(TokenType.ID)) {
            do {
                if (check(TokenType.COMMA)) {
                    consume(TokenType.COMMA);
                }
                params.add(consume(TokenType.ID));
            } while (!isAtEnd() && check(TokenType.COMMA));
        }

        consume(TokenType.RPAREN);
        return params;
    }

    // в полное имя
    private Token toFullName(Token name) {
        return new Token(
            TokenType.TEXT,
            fullNamePrefix + ":" + name.getValue(),
            name.getLine(),
            name.getFileName()
        );
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
                String op;
                Token location;
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
                    default -> throw new WattParsingError(
                            identifier.getLine(),
                            identifier.getFileName(),
                            "operator not found: " + peek(),
                            "check your code."
                    );
                }
                // возвращаем
                VarNode var = new VarNode(prev, identifier);
                var.setShouldPushResult(true);
                return new VarSetNode(prev, identifier, new BinNode(
                        var, expression(), new Token(TokenType.OPERATOR, op,
                        location.getLine(), location.getFileName())
                ));
            } else if (check(TokenType.LPAREN)) {
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
        consume(TokenType.LPAREN);
        Node expr = expression();
        consume(TokenType.RPAREN);
        return expr;
    }

    // анонимная функция
    private Node anonFunction() {
        // локация функции
        Token location = consume(TokenType.FUN);
        // параметры, если есть
        ArrayList<Token> parameters;
        if (check(TokenType.LPAREN)) {
            parameters = params();
        } else {
            parameters = new ArrayList<>();
        }
        // тело
        consume(TokenType.LBRACE);
        BlockNode node = block();
        consume(TokenType.RBRACE);
        // возвращаем анонимную функцию
        return new AnonymousFnNode(
                location,
                node,
                parameters
        );
    }

    // лямбда функия
    private Node lambdaFunction() {
        // локация
        Token location = consume(TokenType.LAMBDA);
        // параметры
        ArrayList<Token> parameters = params();
        // ->
        consume(TokenType.ARROW);
        // выражение
        BlockNode node = BlockNode.of(
                new ReturnNode(
                        location,
                        expression()
                )
        );
        // возвращаем анонимную функцию
        return new AnonymousFnNode(
                location,
                node,
                parameters
        );
    }

    // пайп-оператор
    private AccessNode pipeOperator(AccessNode _expr) {
        // парсим выражение
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
        // проверяем оператор
        if (check(TokenType.PIPE)) {
            while (check(TokenType.PIPE)) {
                // локация
                Token location = consume(TokenType.PIPE);
                // следующее выражение
                if (accessExpr() instanceof CallNode nextCallNode) {
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
            }
        }
        // возвращаем
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
            case TokenType.LPAREN -> {
                return grouping();
            }
            case TokenType.LBRACKET -> {
                return listNode();
            }
            case TokenType.LBRACE -> {
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
        // [
        Token location = consume(TokenType.LBRACKET);
        // список нод внутри
        ArrayList<Node> nodes = new ArrayList<>();
        // если список не пустой
        if (check(TokenType.RBRACKET)) {
            consume(TokenType.RBRACKET);
            return new ListNode(location, nodes);
        }
        // парсим список
        do {
            if (check(TokenType.COMMA)) {
                consume(TokenType.COMMA);
            }
            nodes.add(expression());
        }
        while (check(TokenType.COMMA));
        // ]
        consume(TokenType.RBRACKET);
        // возвращаем
        return new ListNode(location, nodes);
    }

    // словарь нод
    private Node mapNode() {
        // {
        Token location = consume(TokenType.LBRACE);
        // мапа нод внутри
        HashMap<Node, Node> nodes = new HashMap<>();
        // если словарь пуст
        if (check(TokenType.RBRACE)) {
            consume(TokenType.RBRACE);
            return new MapNode(location, nodes);
        }
        // парсим мапу
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
        // }
        consume(TokenType.RBRACE);
        // возвращаем
        return new MapNode(location, nodes);
    }

    // унарное выражение
    private Node unary() {
        // унарный оператор (neg, back)
        if (check(TokenType.OPERATOR) && (match("-") || match("!"))) {
            Token op = consume(TokenType.OPERATOR);
            Node left = primary();
            return new UnaryNode(op, left);
        }

        return primary();
    }

    // рэндж
    private Node range() {
        Node left = unary();

        if (check(TokenType.RANGE)) {
            Token location = consume(TokenType.RANGE);
            left = new RangeNode(
                location,
                left,
                unary()
            );
        }

        return left;
    }

    // умножение, деление
    private Node multiplicative() {
        Node left = range();

        while(check(TokenType.OPERATOR) && (match("*")
                || match("/") || match("%"))) {
            Token operator = consume(TokenType.OPERATOR);
            Node right = range();
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

    // условное выражение
    private Node conditional() {
        Node left = additive();

        if (check(TokenType.GREATER) || check(TokenType.LESS) || check(TokenType.GREATER_EQ) ||
            check(TokenType.LESS_EQ) || check(TokenType.EQUAL) || check(TokenType.NOT_EQUAL)) {
            Token operator = advance();
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
        return check(TokenType.RBRACE);
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
        consume(TokenType.ARROW);
        Token javaName = consume(TokenType.TEXT);
        return new NativeNode(name,toFullName(name),javaName);
    }

    // функция
    private Node function() {
        // адресс и имя
        Token address = consume(TokenType.FUN);
        Token name = consume(TokenType.ID);
        // парсим параметры, если они есть
        ArrayList<Token> parameters;
        if (check(TokenType.LPAREN)) {
             parameters = params();
        } else {
             parameters = new ArrayList<>();
        }
        // тело
        consume(TokenType.LBRACE);
        BlockNode node = block();
        // добавляем возврат null
        node.getNodes().add(new ReturnNode(
                address, new NullNode(address))
        );
        consume(TokenType.RBRACE);
        // возвращаем функцию
        return new FnNode(node,name,toFullName(name),parameters);
    }

    // объявление типа
    private Node type() {
        // type
        consume(TokenType.TYPE);
        // имя
        Token name = consume(TokenType.ID);
        // аргументы конструктора если есть
        ArrayList<Token> constructor = new ArrayList<>();
        if (check(TokenType.LPAREN)) {
            constructor = params();
        }
        // трэйты
        ArrayList<Token> traits = new ArrayList<>();
        if (check(TokenType.IMPL)) {
            consume(TokenType.IMPL);
            do {
                if (check(TokenType.COMMA)) {
                    consume(TokenType.COMMA);
                }
                traits.add(consume(TokenType.ID));
            } while (check(TokenType.COMMA));
        }
        // тело
        consume(TokenType.LBRACE);
        ArrayList<Node> nodes = new ArrayList<>();
        while (!isAtEnd() && !itsClosingBrace()) {
            Node node = statement();
            if (node instanceof FnNode fn) {
                // убираем полное имя, так как это метод
                fn.setFullName(null);
                // добавляем ноду
                nodes.add(fn);
            } else if (node instanceof NativeNode || node instanceof VarDefineNode ||
                    node instanceof VarSetNode) {
                // добавляем ноду
                nodes.add(node);
            }
            else {
                throw new WattParsingError(
                        peek().line,
                        filename,
                        "invalid node token for type: " + peek().type + ":" + peek().value,
                        "available: fun, variable definition; variable set.");
            }
        }
        consume(TokenType.RBRACE);
        // возвращаем
        return new TypeNode(name, toFullName(name), nodes, constructor, traits);
    }

    // объявление юнита
    private Node unit() {
        // unit
        consume(TokenType.UNIT);
        // имя
        Token name = consume(TokenType.ID);
        // тело
        consume(TokenType.LBRACE);
        ArrayList<Node> nodes = new ArrayList<>();
        while (!isAtEnd() && !itsClosingBrace()) {
            Node node = statement();
            if (node instanceof FnNode fn) {
                // убираем полное имя, так как это метод
                fn.setFullName(null);
                // добавляем ноду
                nodes.add(fn);
            } else if (node instanceof NativeNode || node instanceof VarDefineNode ||
                    node instanceof VarSetNode) {
                // добавляем ноду
                nodes.add(node);
            } else {
                throw new WattParsingError(
                        peek().line,
                        filename,
                        "invalid node token for unit: " + peek().type + "::" + peek().value,
                        "available: fun, variable definition; variable set.");
            }
        }
        consume(TokenType.RBRACE);
        // возвращаем
        return new UnitNode(name, toFullName(name), nodes);
    }

    // выражение (рекурсивный парсинг)
    private Node expression() {
        return logical();
    }

    // выражение создания объекта
    private AccessNode objectCreation() {
        // new
        consume(TokenType.NEW);
        // имя типа
        Token name = consume(TokenType.ID);
        // возвращаем ноду
        return new NewInstanceNode(name, args());
    }

    // трэйты
    private Node trait() {
        // trait
        consume(TokenType.TRAIT);
        // имя
        Token name = consume(TokenType.ID);
        // тело
        consume(TokenType.LBRACE);
        // функции трэйта
        ArrayList<TraitNode.TraitFn> fns = new ArrayList<>();
        // парсим функции
        while (!isAtEnd() && !itsClosingBrace()) {
            if (check(TokenType.FUN)) {
                consume(TokenType.FUN);
                Token fnName = this.consume(TokenType.ID);
                // параметры, если есть
                ArrayList<Token> parameters;
                if (check(TokenType.LPAREN)) {
                    parameters = params();
                } else {
                    parameters = new ArrayList<>();
                }
                // имлпементация, если есть
                if (check(TokenType.LBRACE)) {
                    // тело
                    consume(TokenType.LBRACE);
                    BlockNode node = block();
                    consume(TokenType.RBRACE);
                    // добавляем функцию
                    fns.add(new TraitNode.TraitFn(
                        fnName,
                        parameters.size(),
                        new FnNode(
                            node,
                            fnName,
                            null,
                            parameters
                        )
                    ));
                } else {
                    // добавляем функцию
                    fns.add(new TraitNode.TraitFn(
                        fnName,
                        parameters.size(),
                        null
                    ));
                }
            } else {
                throw new WattParsingError(
                    name.getLine(),
                    name.getFileName(),
                    "only fn-s can be declared in trait.",
                    "check your code."
                );
            }
        }
        consume(TokenType.RBRACE);
        // возвращаем
        return new TraitNode(
            name,
            toFullName(name),
            fns
        );
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
            case TokenType.TRY -> {
                return tryNode();
            }
            case TokenType.THROW -> {
                return throwNode();
            }
            case TokenType.TRAIT -> {
                return trait();
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

    // один import
    private ImportNode.WattImport singleImport() {
        // имя импорта
        Token name = consume(TokenType.TEXT);
        // если есть оверрайд полного имени
        if (check(TokenType.WITH)) {
            consume(TokenType.WITH);
            // переписываем полное имя
            return new ImportNode.WattImport(
                name,
                consume(TokenType.TEXT)
            );
        } else {
            // перепись полного имени = null
            return new ImportNode.WattImport(
                name,
                null
            );
        }
    }

    // стэйтмент import
    private Node importNode() {
        // import
        Token location = consume(TokenType.IMPORT);
        // список импортов
        ArrayList<ImportNode.WattImport> imports = new ArrayList<>();
        // если список импортов
        if (check(TokenType.LPAREN)) {
            consume(TokenType.LPAREN);
            do {
                if (check(TokenType.COMMA)) {
                    consume(TokenType.COMMA);
                }
                imports.add(singleImport());
            }
            while (check(TokenType.COMMA));

            consume(TokenType.RPAREN);
        }
        // если импорт 1
        else {
            imports.add(singleImport());
        }
        // возвращаем
        return new ImportNode(location, imports);
    }

    // стэйтмент while
    private Node whileLoop() {
        Token location = consume(TokenType.WHILE);
        Node expr = expression();
        consume(TokenType.LBRACE);
        BlockNode node = block();
        consume(TokenType.RBRACE);
        return new WhileNode(location, node, expr);
    }

    // стэйтмент try
    private Node tryNode() {
        // try
        consume(TokenType.TRY);
        consume(TokenType.LBRACE);
        BlockNode tryNode = block();
        consume(TokenType.RBRACE);
        // catch
        consume(TokenType.CATCH);
        Token catchName = consume(TokenType.ID);
        consume(TokenType.LBRACE);
        BlockNode catchNode = block();
        consume(TokenType.RBRACE);
        // возвращаем ноду
        return new TryNode(
            tryNode,
            catchNode,
            catchName
        );
    }

    // стэйтмент throw
    private Node throwNode() {
        Token location = consume(TokenType.THROW);
        return new ThrowNode(
            location,
            expression()
        );
    }

    // стэйтмент else
    private IfNode elseNode() {
        // локация
        Token location = consume(TokenType.ELSE);
        // тело
        consume(TokenType.LBRACE);
        BlockNode node = block();
        consume(TokenType.RBRACE);
        // возвращаем
        return new IfNode(location, node, new BoolNode(new Token(TokenType.BOOL, "true", location.line, filename)), null);
    }

    // стэйтмент elif
    private IfNode elifNode() {
        // локация
        Token location = consume(TokenType.ELIF);
        // логическое выражение
        Node logical = expression();
        // тело
        consume(TokenType.LBRACE);
        BlockNode node = block();
        consume(TokenType.RBRACE);
        // нода
        IfNode ifNode = new IfNode(location, node, logical, null);
        // else нода
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
        // локация
        Token location = consume(TokenType.IF);
        // логическое выражение
        Node logical = expression();
        // тело
        consume(TokenType.LBRACE);
        BlockNode node = block();
        consume(TokenType.RBRACE);
        // нода
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
        // for
        consume(TokenType.FOR);
        // айди
        Token name = consume(TokenType.ID);
        // in
        consume(TokenType.IN);
        // выражение
        Node range = expression();
        // тело
        consume(TokenType.LBRACE);
        BlockNode node = block();
        consume(TokenType.RBRACE);
        // возвращаем
        return new ForNode(node, name, range);
    }

    // выражение match
    private Node matchExpr() {
        // match
        Token location = consume(TokenType.MATCH);
        // выражение для матча
        Node matchable = expression();
        // кейсы
        List<MatchNode.Case> cases = new ArrayList<>();
        MatchNode.Case defaultCase;
        consume(TokenType.LBRACE);
        // парсинг кейсов
        while (check(TokenType.CASE)) {
            consume(TokenType.CASE);
            Node equality = expression();
            consume(TokenType.ARROW);
            cases.add(
                    new MatchNode.Case(
                            equality,
                            expression()
                    )
            );
        }
        // парсинг дефолтного кейса
        if (check(TokenType.DEFAULT)) {
            consume(TokenType.DEFAULT);
            consume(TokenType.ARROW);
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
        consume(TokenType.RBRACE);
        // возвращаем
        return new MatchNode(
                location,
                matchable,
                cases,
                defaultCase
        );
    }

    // стэйтмент match
    private Node matchStmt() {
        // match
        Token location = consume(TokenType.MATCH);
        // выражение для матча
        Node matchable = expression();
        // кейсы
        List<MatchNode.Case> cases = new ArrayList<>();
        MatchNode.Case defaultCase = null;
        consume(TokenType.LBRACE);
        // парсинг кейсов
        while (check(TokenType.CASE)) {
            consume(TokenType.CASE);
            Node equality = expression();
            if (check(TokenType.ARROW)) {
                consume(TokenType.ARROW);
                cases.add(new MatchNode.Case(equality, statement()));
            } else {
                consume(TokenType.LBRACE);
                cases.add(new MatchNode.Case(equality, block()));
                consume(TokenType.RBRACE);
            }
        }
        // парсинг дефолтного кейса
        if (check(TokenType.DEFAULT)) {
            consume(TokenType.DEFAULT);
            if (check(TokenType.ARROW)) {
                defaultCase = new MatchNode.Case(null, statement());
            } else {
                consume(TokenType.LBRACE);
                defaultCase = new MatchNode.Case(null, block());
                consume(TokenType.RBRACE);
            }
        }
        consume(TokenType.RBRACE);
        // возврощаем
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

    // получаем текущий токен и прыгаем дальше
    private Token advance() {
        if (isAtEnd()) {
            Token token = this.tokenList.get(current-1);
            throw new WattParsingError(
                    token.line,
                    filename,
                    "couldn't advance token.",
                    "its end of file! last token: " + token.type + ":" + token.value);
        } else {
            Token tk = this.tokenList.get(current);
            current += 1;
            return tk;
        }
    }
}
