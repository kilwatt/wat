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
    /*
    Сущеость импорта
     */
    @Getter
    public static class WattImport {
        // как модуль?
        private final boolean shouldImportAsModule;
        // имя файла?
        private final Token name;
        // имя модуля?
        private final Token module;

        public WattImport(Token name, Token module) {
            this.name = name;
            this.module = module;
            this.shouldImportAsModule = true;
        }

        public WattImport(Token name) {
            this.name = name;
            this.module = null;
            this.shouldImportAsModule = false;
        }
    }

    // импорты
    private final ArrayList<WattImport> imports;

    // компиляция
    @Override
    public void compile() {
        for (WattImport singleImport : imports) {
            WattExecutor.getImportsResolver().resolve(singleImport);
        }
    }
}
