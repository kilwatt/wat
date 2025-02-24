package com.kea.Executor;

import com.kea.Compiler.KeaBuiltinProvider;
import com.kea.Compiler.KeaCompiler;
import com.kea.Errors.KeaError;
import com.kea.Errors.KeaParsingError;
import com.kea.Errors.KeaRuntimeError;
import com.kea.KeaVM.VmAddress;
import com.kea.Lexer.Lexer;
import com.kea.Parser.AST.BlockNode;
import com.kea.Parser.AST.Node;
import com.kea.Parser.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/*
Экзекьютер
 */
public class KeaExecutor {
    public static void run(String path) throws IOException {
        // пробуем
        try {
            // парсим
            Lexer lexer = new Lexer("test.kea", new String(Files.readAllBytes(Path.of(path))));
            Parser parser = new Parser("test.kea", lexer.scan());
            Node result = parser.parse();
            // компилируем
            KeaCompiler.compile(result);
            // объявляем функции
            KeaBuiltinProvider.provide();
            // запускаем код
            KeaCompiler.vm.run(KeaCompiler.code);
        } catch (KeaParsingError | KeaRuntimeError error) {
            // если есть ошибка - выводим
            error.print();
            // error.printStackTrace();
        }
    }
}
