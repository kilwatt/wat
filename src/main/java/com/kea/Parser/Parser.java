package com.kea.Parser;

import com.kea.Errors.KeaParsingError;
import com.kea.Lexer.Token;
import com.kea.Lexer.TokenType;
import com.kea.Parser.AST.*;
import lombok.Getter;

import java.util.ArrayList;

/*
Парсер
 */
@Getter
public class Parser {
    private final String filename;
    private final ArrayList<Token> tokenList;
    private int current = 0;

    public Parser(String filename, ArrayList<Token> tokenList) {
        this.filename = filename;
        this.tokenList = tokenList;
    }

    public Node parse() {
        return block();
    }

    private ArrayList<Node> args() {
        ArrayList<Node> nodes = new ArrayList<>();
        consume(TokenType.LEFT_PAREN);

        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                nodes.add(expression());
            } while (!isAtEnd() && check(TokenType.COMMA));
        }

        consume(TokenType.RIGHT_PAREN);
        return nodes;
    }

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

    private AccessNode accessPart(AccessNode prev) {
        Token identifier = consume(TokenType.ID);
        if (check(TokenType.WALRUS)) {
            consume(TokenType.WALRUS);
            return new VarDefineNode(prev, identifier, expression());
        } else if (check(TokenType.ASSIGN)) {
            consume(TokenType.ASSIGN);
            return new VarSetNode(prev, identifier, expression());
        } else if (check(TokenType.LEFT_PAREN)) {
            return new CallNode(prev, identifier, args());
        } else {
            return new VarNode(prev, identifier);
        }
    }

    private AccessNode accessExpr() {
        AccessNode left = accessPart(null);

        while (check(TokenType.DOT)) {
            consume(TokenType.DOT);
            left.setShouldPushResult(true);
            left = accessPart(left);
            if (left instanceof VarDefineNode defineNode) {
                throw new KeaParsingError(
                        defineNode.getName().line,
                        filename,
                        "Cannot use define like expression!",
                        "Check your code!");
            }
        }

        left.setShouldPushResult(true);
        return left;
    }

    private Node accessStatement() {
        AccessNode left = accessPart(null);

        while (check(TokenType.DOT)) {
            consume(TokenType.DOT);
            left.setShouldPushResult(true);
            left = accessPart(left);
            if (left instanceof VarDefineNode defineNode) {
                break;
            }
        }

        left.setShouldPushResult(false);
        return left;
    }

    private Node grouping() {
        consume(TokenType.LEFT_PAREN);
        Node expr = expression();
        consume(TokenType.RIGHT_PAREN);
        return expr;
    }

    private Node primary() {
        switch (peek().type) {
            case ID -> {
                return accessExpr();
            }
            case NEW -> {
                return objectCreation();
            }
            case NUM -> {
                return new NumberNode(consume(TokenType.NUM));
            }
            case TEXT -> {
                return new StringNode(consume(TokenType.TEXT));
            }
            case BOOL -> {
                return new BoolNode(consume(TokenType.BOOL));
            }
            case LEFT_PAREN -> {
                return grouping();
            }
            default -> {
               throw new KeaParsingError(
                        peek().line,
                        filename,
                        "Invalid token for primary parsing: " + peek().type + "::" + peek().value,
                       "Did you write wrong expression?");
            }
        }
    }

    private Node multiplicative() {
        Node left = primary();

        if (check(TokenType.OPERATOR) && (match("*") || match("/"))) {
            Token operator = consume(TokenType.OPERATOR);
            Node right = primary();
            return new BinNode(left, right, operator);
        }

        return left;
    }

    private Node additive() {
        Node left = multiplicative();

        if (check(TokenType.OPERATOR) && (match("+") || match("-"))) {
            Token operator = consume(TokenType.OPERATOR);
            Node right = multiplicative();
            return new BinNode(left, right, operator);
        }

        return left;
    }

    private Token conditionalOperator() {
        return switch (peek().type) {
            case EQUAL -> consume(TokenType.EQUAL);
            case NOT_EQUAL -> consume(TokenType.NOT_EQUAL);
            case LOWER -> consume(TokenType.LOWER);
            case BIGGER -> consume(TokenType.BIGGER);
            case LOWER_EQUAL -> consume(TokenType.LOWER_EQUAL);
            case BIGGER_EQUAL -> consume(TokenType.BIGGER_EQUAL);
            default -> {
                throw new KeaParsingError(
                        peek().line,
                        filename,
                        "Invalid conditional operator! " + peek().type + "::" + peek().value,
                        "Available operators: ==, !=, >, <, >=, <=");
            }
        };
    }

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

    private Node logical() {
        Node left = conditional();

        if (check(TokenType.AND)) {
            Token operator = consume(TokenType.AND);
            Node right = conditional();
            return new LogicalNode(left, right, operator);
        }
        else if (check(TokenType.OR)) {
            Token operator = consume(TokenType.OR);
            Node right = conditional();
            return new LogicalNode(left, right, operator);
        }

        return left;
    }

    private boolean itsClosingBrace() {
        return check(TokenType.RIGHT_BRACE);
    }

    private BlockNode block() {
        ArrayList<Node> nodes = new ArrayList<>();

        while (!isAtEnd() && !itsClosingBrace()) {
            nodes.add(statement());
        }

        return new BlockNode(nodes);
    }

    private Node function() {
        consume(TokenType.FUNC);
        Token name = consume(TokenType.ID);
        ArrayList<Token> parameters = params();
        consume(TokenType.GO);
        consume(TokenType.LEFT_BRACE);
        BlockNode node = block();
        consume(TokenType.RIGHT_BRACE);
        return new FnNode(node,name,parameters);
    }

    private Node type() {
        consume(TokenType.TYPE);
        Token name = consume(TokenType.ID);
        consume(TokenType.GO);
        consume(TokenType.LEFT_BRACE);
        ArrayList<Node> nodes = new ArrayList<>();
        while (!isAtEnd() && !itsClosingBrace()) {
            Node node = statement();
            if (node instanceof FnNode || node instanceof VarDefineNode ||
                node instanceof VarSetNode) {
                nodes.add(node);
            } else {
                throw new KeaParsingError(
                        peek().line,
                        filename,
                        "Invalid node while type parsing: " + peek().type + "::" + peek().value,
                        "Available: function, variable set, variable define.");
            }
        }
        consume(TokenType.RIGHT_BRACE);
        return new TypeNode(name, nodes);
    }

    private Node unit() {
        consume(TokenType.UNIT);
        Token name = consume(TokenType.ID);
        consume(TokenType.GO);
        consume(TokenType.LEFT_BRACE);
        ArrayList<Node> nodes = new ArrayList<>();
        while (!isAtEnd() && !itsClosingBrace()) {
            Node node = statement();
            if (node instanceof FnNode || node instanceof VarDefineNode ||
                    node instanceof VarSetNode) {
                nodes.add(node);
            } else {
                throw new KeaParsingError(
                        peek().line,
                        filename,
                        "Invalid node while type parsing: " + peek().type + "::" + peek().value,
                        "Available: function, variable set, variable define.");
            }
        }
        consume(TokenType.RIGHT_BRACE);
        return new UnitNode(name, nodes);
    }

    private Node expression() {
        return logical();
    }

    private Node objectCreation() {
        consume(TokenType.NEW);
        Token name = consume(TokenType.ID);
        return new NewInstanceNode(name, args());
    }

    private Node statement() {
        switch (peek().type) {
            case TokenType.ID -> {
                return accessStatement();
            }
            case TokenType.FUNC -> {
                return function();
            }
            case TokenType.NEW -> {
                return objectCreation();
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
            /*
            case TokenType.MATCH -> {
                return matchNode();
            }
             */
            case TokenType.CONTINUE -> {
                return continueNode();
            }
            case TokenType.BREAK -> {
                return breakNode();
            }
            case TokenType.ASSERT -> {
                return assertion();
            }
            default -> throw new KeaParsingError(
                    peek().line,
                    filename,
                    "Unexpected token for statement: " + peek(),
                    "Check your code!");
        }
    }

    private Node continueNode() {
        Token loc = consume(TokenType.CONTINUE);
        return new ContinueNode(loc);
    }

    private Node breakNode() {
        Token loc = consume(TokenType.BREAK);
        return new BreakNode(loc);
    }

    private Node assertion() {
        Token loc = consume(TokenType.ASSERT);
        return new AssertNode(loc, expression());
    }

    private Node whileLoop() {
        Token location = consume(TokenType.WHILE);
        Node expr = expression();
        consume(TokenType.LEFT_BRACE);
        BlockNode node = block();
        consume(TokenType.RIGHT_BRACE);
        return new WhileNode(location, node, expr);
    }

    private IfNode elseNode() {
        Token elseTok = consume(TokenType.ELSE);
        consume(TokenType.RIGHT_BRACE);
        BlockNode node = block();
        consume(TokenType.RIGHT_BRACE);
        return new IfNode(elseTok, node, new BoolNode(new Token(TokenType.BOOL, "true", elseTok.line, filename)), null);
    }

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

    private Node ifNode() {
        Token location = consume(TokenType.ELIF);
        consume(TokenType.IF);
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

    private Node forLoop() {
        consume(TokenType.FOR);
        Token name = consume(TokenType.ID);
        consume(TokenType.IN);
        Node from = expression();
        System.out.println(from);
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

    private boolean isAtEnd() {
        return current >= tokenList.size();
    }

    private boolean check(TokenType expected) {
        if (isAtEnd()) { return false; }
        return this.tokenList.get(current).type == expected;
    }

    private boolean match(String value) {
        if (isAtEnd()) { return false; }
        return this.tokenList.get(current).value.equals(value);
    }

    private Token consume(TokenType expected) {
        if (isAtEnd()) {
            Token token = this.tokenList.get(current-1);
            throw new KeaParsingError(
                token.line,
                filename,
                "Can't consume token because of file end!",
                "Check your file! Last token: " + token.type + "::" + token.value);
        }
        Token token = this.tokenList.get(current);
        if (token.type == expected) {
            current += 1;
            return token;
        } else {
            throw new KeaParsingError(
                    token.line,
                    filename,
                    "Unexpected token: " + token.type + "::" + token.value,
                    "Did you forget to use token " + expected + "?");
        }
    }

    private Token peek() {
        if (isAtEnd()) {
            Token token = this.tokenList.get(current-1);
            throw new KeaParsingError(
                    token.line,
                    filename,
                    "Can't consume token because of file end!",
                    "Check your file! Last token: " + token.type + "::" + token.value);
        } else {
            return this.tokenList.get(current);
        }
    }
}
