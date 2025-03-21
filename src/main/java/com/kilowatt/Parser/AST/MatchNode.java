package com.kilowatt.Parser.AST;

import com.kilowatt.Errors.WattParsingError;
import com.kilowatt.Lexer.Token;
import com.kilowatt.Lexer.TokenType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/*
Pattern Matching Node
 */
@Getter
@AllArgsConstructor
public class MatchNode implements Node {
    private final Token location;
    private final Node matchableValue;
    private final List<Case> cases;
    private final Case defaultCase;

    /*
    Кейс
     */
    @AllArgsConstructor
    @Getter
    public static class Case {
        private final Node equality;
        private final Node body;
    }

    /*
    Компиляция
     */
    @Override
    public void compile() {
        // последнее условие
        IfNode last = null;
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
            if (last != null) {
                last.setElseNode(newIfNode);
                last = newIfNode;
            } else {
                last = newIfNode;
            }
        }
        // компилируем
        if (last != null) {
            // создаём default
            if (defaultCase != null) {
                last.setElseNode(new IfNode(
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
            }
            // компилируем if
            last.compile();
        } else {
            throw new WattParsingError(
                    location.getLine(),
                    location.getFileName(),
                    "couldn't compile match with no cases.",
                    "add some cases for match."
            );
        }
    }
}
