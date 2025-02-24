package com.kea.Parser.AST;

import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/*
Импорт
 */
@Getter
@AllArgsConstructor
public class ImportNode implements Node {
    private final Token name;

    @Override
    public void compile() {

    }
}
