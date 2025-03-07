package com.kilowatt.Parser.AST;

import com.kilowatt.Executor.WattExecutor;
import com.kilowatt.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Импорт
 */
@Getter
@AllArgsConstructor
public class ImportNode implements Node {
    private final Token name;

    @Override
    public void compile() {
        WattExecutor.resolve(name.asAddress(), name.value);
    }
}
