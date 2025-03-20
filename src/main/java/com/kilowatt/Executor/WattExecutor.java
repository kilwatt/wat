package com.kilowatt.Executor;

import com.kilowatt.Compiler.Builtins.WattLibraries;
import com.kilowatt.Compiler.WattBuiltinProvider;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattParsingError;
import com.kilowatt.Errors.WattResolveError;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.Lexer.Lexer;
import com.kilowatt.Parser.AST.BlockNode;
import com.kilowatt.Parser.AST.Node;
import com.kilowatt.Parser.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/*
Экзекьютер
 */
@SuppressWarnings({"resource", "DataFlowIssue"})
public class WattExecutor {
    // локальный путь
    private static Path localPath;

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
            // компилируем
            WattCompiler.compile(result);
            // объявляем функции
            WattBuiltinProvider.provide();
            // запускаем код
            WattCompiler.vm.run(WattCompiler.code, true);
        } catch (WattParsingError | WattRuntimeError | WattResolveError error) {
            // если есть ошибка - выводим
            error.print();
            // error.printStackTrace();
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
        try {
            if (!WattLibraries.libraries.containsKey(name)) {
                fileName = name;
                lexer = new Lexer(name, new String(Files.readAllBytes(localPath.resolve(name))));
            } else {
                try {
                    fileName = WattLibraries.libraries.get(name);
                    InputStream stream = WattExecutor.class.getResourceAsStream("/" + fileName);
                    lexer = new Lexer(fileName, new String(stream.readAllBytes()));
                } catch (NullPointerException e) {
                    throw new WattResolveError(
                            address.getLine(),
                            address.getFileName(),
                            "Can't resolve name: " + name,
                            "Check file exists!");
                }
            }
        } catch (IOException e) {
            throw new WattResolveError(
                    address.getLine(),
                    address.getFileName(),
                    "Can't resolve name: " + localPath.resolve(name),
                    "Check file exists!");
        }
        Parser parser = new Parser(fileName, lexer.scan());
        BlockNode result = parser.parse();
        // компилируем
        WattCompiler.importDefinitions(result);
    }
}
