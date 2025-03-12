package com.kilowatt.Parser.AST;

import com.kilowatt.Executor.WattExecutor;
import com.kilowatt.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

/*
Импорт
 */
@Getter
@AllArgsConstructor
public class ImportNode implements Node {
    private final ArrayList<Token> names;

    @Override
    public void compile() {
        for (Token name : names) {
            WattExecutor.resolve(name.asAddress(), name.value);
        }
    }
}
