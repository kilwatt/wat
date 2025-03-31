package com.kilowatt.Parser.AST;

import com.kilowatt.Errors.WattParsingError;
import com.kilowatt.Lexer.Token;
import com.kilowatt.Lexer.TokenType;
import com.kilowatt.Semantic.SemanticAnalyzer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/*
Pattern Matching Node
 */
@Getter
@AllArgsConstructor
public class MatchNode implements Node {
    // локация
    private final Token location;
    // нода для матчинга
    private final Node matchableValue;
    // кейсы
    private final List<Case> cases;
    // дефолтный кейс
    private final Case defaultCase;

    /*
    Кейс
     */
    @AllArgsConstructor
    @Getter
    public static class Case {
        // сравниваемое значение
        private final Node equality;
        // тело кейса
        private final Node body;
    }

    /*
    Компиляция
     */
    @Override
    public void compile() {
        // последний кейс
        IfNode lastCase = null;
        // рутовый кейс (первая)
        IfNode rootCase = null;
        // собираем кейсы и превращаем в IF
        for (Case _case : cases) {
            IfNode newIfNode = new IfNode(
                location,
                BlockNode.of(
                    _case.body
                ),
                new ConditionalNode(
                    matchableValue,
                    _case.equality,
                    new Token(
                        TokenType.OPERATOR,
                        "==",
                        location.getLine(),
                        location.getFileName()
                    )
                ),
                null
            );
            // если первая нода - значит рутовый кейс
            if (rootCase == null) {
                rootCase = newIfNode;
            }
            // если есть предыдущий кейс - устанавливаем ему else на текущий
            if (lastCase != null) {
                lastCase.setElseNode(newIfNode);
            }
            // обновляем предыдущий кейс на текущий
            lastCase = newIfNode;
        }
        // компилируем ноду
        if (rootCase != null) {
            // создаём default
            if (defaultCase != null) {
                lastCase.setElseNode(new IfNode(
                    location,
                    defaultCase.getBody(),
                    new BoolNode(
                            new Token(
                                    TokenType.BOOL,
                                    "true",
                                    location.getLine(),
                                    location.getFileName()
                            )
                    ),
                    null
                ));
            } else {
                throw new WattParsingError(
                    location.getLine(),
                    location.getFileName(),
                    "couldn't compile match with no default case.",
                    "add default case for match."
                );
            }
            // компилируем if
            rootCase.compile();
        } else {
            throw new WattParsingError(
                location.getLine(),
                location.getFileName(),
                "couldn't compile match with no cases.",
                "add some cases for match."
            );
        }
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        // анализируем значение для матча
        matchableValue.analyze(analyzer);
        // анализируем кейсы
        for (Case _case : cases) {
            _case.getBody().analyze(analyzer);
            _case.getEquality().analyze(analyzer);
        }
        // анализируем дефолтный кейс
        if (defaultCase != null) {
            defaultCase.getBody().analyze(analyzer);
        }
    }
}
