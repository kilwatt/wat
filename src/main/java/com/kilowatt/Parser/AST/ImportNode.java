package com.kilowatt.Parser.AST;

import com.kilowatt.Errors.WattSemanticError;
import com.kilowatt.Executor.WattExecutor;
import com.kilowatt.Lexer.Token;
import com.kilowatt.Semantic.SemanticAnalyzer;
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

    // локация
    private final Token location;

    // импорты
    private final ArrayList<WattImport> imports;

    // компиляция
    @Override
    public void compile() {
        for (WattImport wattImport : imports) {
            // получаем оверрайд полного имени
            String fullNameOverride = wattImport.fullNameOverride != null ?
                    wattImport.fullNameOverride.getValue() : null;
            // ресолв
            WattExecutor.getImportsResolver().resolve(
                wattImport.name.asAddress(),
                wattImport.name.getValue(),
                fullNameOverride
            );
        }
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        if (analyzer.top() != null) {
            throw new WattSemanticError(
                location.getLine(),
                location.getFileName(),
                "couldn't use import outside global code",
                "move this imports."
            );
        }
    }
}
