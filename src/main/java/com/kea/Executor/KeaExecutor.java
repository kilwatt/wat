package com.kea.Executor;

import com.kea.Compiler.KeaBuiltinProvider;
import com.kea.Compiler.KeaCompiler;
import com.kea.Errors.KeaError;
import com.kea.Errors.KeaParsingError;
import com.kea.Errors.KeaResolveError;
import com.kea.Errors.KeaRuntimeError;
import com.kea.KeaVM.VmAddress;
import com.kea.Lexer.Lexer;
import com.kea.Lexer.Token;
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
    private static Path localPath;

    public static void run(String path) throws IOException {
        // пробуем
        try {
            // путь
            Path filePath = Path.of(path);
            localPath = filePath.getParent();
            // парсим
            Lexer lexer = new Lexer(filePath.getFileName().toString(), new String(Files.readAllBytes(filePath)));
            Parser parser = new Parser(filePath.getFileName().toString(), lexer.scan());
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

    // импорт файла
    public static void resolve(VmAddress address, String name) {
        // парсим
        Lexer lexer = null;
        try {
            lexer = new Lexer("test.kea", new String(Files.readAllBytes(localPath.resolve(name))));
        } catch (IOException e) {
            throw new KeaResolveError(
                    address.getLine(),
                    address.getFileName(),
                    "Can't resolve name" + localPath.resolve(name).toString(),
                    "Check file exists!");
        }
        Parser parser = new Parser(name, lexer.scan());
        BlockNode result = parser.parse();
        // компилируем
        KeaCompiler.importDefinitions(result);
    }
}
