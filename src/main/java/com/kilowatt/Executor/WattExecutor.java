package com.kilowatt.Executor;

import com.kilowatt.Compiler.WattBuiltinProvider;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattParsingError;
import com.kilowatt.Errors.WattResolveError;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.Errors.WattSemanticError;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.Lexer.Lexer;
import com.kilowatt.Parser.AST.Node;
import com.kilowatt.Parser.Parser;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/*
Экзекьютер
 */
public class WattExecutor {
    // локальный путь
    @Getter
    private static Path localPath;
    // ресолвер импортов
    @Getter
    private static WattImportResolver importsResolver;

    // запуск
    public static void run(String path) throws IOException {
        // пробуем
        try {
            // путь
            Path filePath = Path.of(path);
            localPath = filePath.getParent();
            // ресолвер импортов
            importsResolver = new WattImportResolver(localPath);
            // парсим
            Lexer lexer = new Lexer(filePath.getFileName().toString(), new String(Files.readAllBytes(filePath)));
            Parser parser = new Parser(filePath.getFileName().toString(), lexer.scan());
            Node result = parser.parse();
            // семантический анализ
            SemanticAnalyzer analyzer = new SemanticAnalyzer();
            analyzer.analyze(result);
            // компилируем
            WattCompiler.compile(result);
            // объявляем функции
            WattBuiltinProvider.provide();
            // запускаем код
            WattCompiler.vm.run(WattCompiler.code, true);
        } catch (WattParsingError | WattRuntimeError |
                 WattResolveError | WattSemanticError error) {
            // если есть ошибка - выводим
            error.print();
            // error.printStackTrace();
        } catch (RuntimeException error) {
            VmAddress address = WattCompiler.vm.getLastCallAddress();
            new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "jvm runtime exception: " + error.getMessage(),
                    "check your code."
            ).print();
        }
    }

    public static void test(String path) throws IOException {
        // путь
        Path filePath = Path.of(path);
        localPath = filePath.getParent();
        // ресолвер импортов
        importsResolver = new WattImportResolver(localPath);
        // парсим
        Lexer lexer = new Lexer(filePath.getFileName().toString(), new String(Files.readAllBytes(filePath)));
        Parser parser = new Parser(filePath.getFileName().toString(), lexer.scan());
        Node result = parser.parse();
        // семантический анализ
        SemanticAnalyzer analyzer = new SemanticAnalyzer();
        analyzer.analyze(result);
        // компилируем
        WattCompiler.compile(result);
        // объявляем функции
        WattBuiltinProvider.provide();
        // запускаем код
        WattCompiler.vm.run(WattCompiler.code, false);
    }
}
