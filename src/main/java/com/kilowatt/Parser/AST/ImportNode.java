package com.kilowatt.Parser.AST;

import com.kilowatt.Executor.WattExecutor;
import com.kilowatt.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/*
Импорт
 */
@Getter
@AllArgsConstructor
public class ImportNode implements Node {
    /*
    Класс импорта
     */
    @AllArgsConstructor
    @Setter
    public static class WattImport {
        private Token name;
        private Token fullNameOverride;
    }

    // импорты
    private final ArrayList<WattImport> imports;

    // компиляция
    @Override
    public void compile() {
        for (WattImport wattImport : imports) {
            WattExecutor.getImportsResolver().resolve(
                wattImport.name.asAddress(),
                wattImport.name.getValue(),
                wattImport.fullNameOverride != null ?
                        wattImport.fullNameOverride.getValue() : null
            );
        }
    }
}
