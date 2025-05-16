package com.kilowatt.Executor;

import com.kilowatt.Commands.WattCommandError;
import com.kilowatt.Compiler.WattBuiltinProvider;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.*;
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
    // переданные аргументы
    @Getter
    private static String[] passedArgs;
    // ресолвер импортов
    @Getter
    private static WattImportResolver importsResolver;

    // запуск скрипта
    public static void run(String path, String[] args) throws IOException {
        // путь
        Path filePath = Path.of(path);
        localPath = filePath.getParent();
        // проверяем файл на существование
        if (!Files.exists(filePath)) {
            throw new WattCommandError("File: " + path + " not found.");
        }
        // пробуем
        try {
            // аргументы
            passedArgs = args;
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
            WattCompiler.reset();
            WattCompiler.compile(filePath.getFileName().toString(), result, false);
            // объявляем функции
            WattBuiltinProvider.provide();
            // запускаем код
            WattCompiler.vm.run(WattCompiler.code, true);
        } catch (WattError error) {
            // если есть ошибка - выводим
            error.panic();
        } catch (RuntimeException error) {
            if (WattCompiler.vm.getCallsTrace() != null && !WattCompiler.vm.getCallsHistory().isEmpty()) {
                // адрес
                VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
                // ошибка
                new WattInternalError(
                    address,
                    "jvm runtime exception: " + error.getMessage(),
                    "check your code.",
                    error.getStackTrace()
                ).panic();
            } else {
                // ошибка
                new WattInternalError(
                    new VmAddress("no-file", -1, -1, "no-file"),
                    "jvm runtime exception: " + error.getMessage(),
                    "check your code.",
                    error.getStackTrace()
                ).panic();
            }
        }
    }

    // дамп байткода скрипта
    public static void dump(String path, boolean asFile) throws IOException {
        // путь
        Path filePath = Path.of(path);
        localPath = filePath.getParent();
        // проверяем файл на существование
        if (!Files.exists(filePath)) {
            throw new WattCommandError("File: " + path + " not found.");
        }
        // пробуем
        try {
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
            WattCompiler.reset();
            WattCompiler.compile(filePath.getFileName().toString(), result, false);
            // объявляем функции
            WattBuiltinProvider.provide();
            // дампим код
            WattCompiler.vm.dump(WattCompiler.code, asFile);
        } catch (RuntimeException error) {
            if (WattCompiler.vm.getCallsTrace() != null && !WattCompiler.vm.getCallsHistory().isEmpty()) {
                // адрес
                VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
                // ошибка
                new WattInternalError(
                    address,
                    "jvm runtime exception: " + error.getMessage(),
                    "check your code.",
                    error.getStackTrace()
                ).panic();
            } else {
                // ошибка
                new WattInternalError(
                    new VmAddress("no-file", -1, -1, "no-file"),
                    "jvm runtime exception: " + error.getMessage(),
                    "check your code.",
                    error.getStackTrace()
                ).panic();
            }
        }
    }

    public static void test(String path) throws IOException {
        // путь
        Path filePath = Path.of(path);
        localPath = filePath.getParent();
        // проверяем файл на существование
        if (!Files.exists(filePath)) {
            throw new WattCommandError("File: " + path + " not found.");
        }
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
        WattCompiler.reset();
        WattCompiler.compile(filePath.getFileName().toString(), result, false);
        // объявляем функции
        WattBuiltinProvider.provide();
        // запускаем код
        WattCompiler.vm.run(WattCompiler.code, false);
    }
}