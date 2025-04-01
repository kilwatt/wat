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
    // импорты
    private final ArrayList<Token> imports;

    // компиляция
    @Override
    public void compile() {
        for (Token name : imports) {
            WattExecutor.getImportsResolver().resolve(
                name.asAddress(),
                name.getValue()
            );
        }
    }
}
