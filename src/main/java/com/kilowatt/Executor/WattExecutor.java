package com.kilowatt.Executor;

import com.kilowatt.Compiler.Builtins.WattLibraries;
import com.kilowatt.Compiler.WattBuiltinProvider;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattParsingError;
import com.kilowatt.Errors.WattResolveError;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.Errors.WattSemanticError;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.Lexer.Lexer;
import com.kilowatt.Parser.AST.BlockNode;
import com.kilowatt.Parser.AST.Node;
import com.kilowatt.Parser.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/*
Экзекьютер
 */
@SuppressWarnings({"resource", "DataFlowIssue"})
public class WattExecutor {
    // локальный путь
    private static Path localPath;
    // импортированные пути
    private static final List<String> imported = new ArrayList<>();

    // запуск
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

    // импорт файла
    public static void resolve(VmAddress address, String name) {
        // парсим
        Lexer lexer;
        String fileName;
        String pathString;
        try {
            if (!WattLibraries.libraries.containsKey(name)) {
                fileName = name;
                Path path = localPath.resolve(name);
                pathString = path.toString();
                lexer = new Lexer(name, new String(Files.readAllBytes(path)));
            } else {
                try {
                    fileName = WattLibraries.libraries.get(name);
                    pathString = "/" + fileName;
                    InputStream stream = WattExecutor.class.getResourceAsStream(pathString);
                    lexer = new Lexer(fileName, new String(stream.readAllBytes()));
                } catch (NullPointerException e) {
                    throw new WattResolveError(
                            address.getLine(),
                            address.getFileName(),
                            "couldn't resolve name: " + name,
                            "check file exists!");
                }
            }
        } catch (IOException e) {
            throw new WattResolveError(
                    address.getLine(),
                    address.getFileName(),
                    "couldn't resolve name: " + localPath.resolve(name),
                    "check file exists!");
        }
        Parser parser = new Parser(fileName, lexer.scan());
        BlockNode result = parser.parse();
        // компилируем
        if (!imported.contains(pathString)) {
            WattCompiler.importDefinitions(result);
            imported.add(pathString);
        }
    }
}
