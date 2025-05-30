package com.kilowatt.Executor;

import com.kilowatt.Compiler.Builtins.WattLibraries;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattResolveError;
import com.kilowatt.Lexer.Lexer;
import com.kilowatt.Parser.AST.BlockNode;
import com.kilowatt.Parser.Parser;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.VmAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/*
Ресолвер импортов
 */
@AllArgsConstructor
@Getter
public class WattImportResolver {
    // импортированные пути
    private final List<String> imported = new ArrayList<>();
    // локальный путь
    private final Path localPath;

    // импорт файла
    public void resolve(VmAddress address, String name, String fullNameOverride) {
        // данные
        Lexer lexer;
        String fileName;
        String pathString;
        // загружаем файл
        try {
            // проверяем библиотеки
            if (!WattLibraries.libraries.containsKey(name)) {
                // имя файла
                fileName = name;
                // путь
                Path path = localPath.resolve(name);
                // строка пути
                pathString = path.toString();
                // лексер
                lexer = new Lexer(name, new String(Files.readAllBytes(path)));
            } else {
                // строка пути
                pathString = "/" + WattLibraries.libraries.get(name);
                // имя файла
                fileName = Path.of(pathString).getFileName().toString();
                // чтение
                try (InputStream stream = WattExecutor.class.getResourceAsStream(pathString)) {
                    if (stream != null) {
                        lexer = new Lexer(fileName, new String(stream.readAllBytes()));
                    } else {
                        throw new WattResolveError(
                            address,
                            "couldn't resolve name: " + name,
                            "check file exists!");
                    }
                } catch (NullPointerException e) {
                    throw new WattResolveError(
                        address,
                        "couldn't resolve name: " + name,
                        "check file exists!");
                }
            }
        }
        // в случае ошибки
        catch (IOException e) {
            throw new WattResolveError(
                address,
                "couldn't resolve name: " + localPath.resolve(name),
                "check file exists!"
            );
        }
        // парсер
        Parser parser = new Parser(fileName, lexer.scan());
        // оверрайд полного имени
        if (fullNameOverride != null) parser.setFullNamePrefix(fullNameOverride);
        // парсинг
        BlockNode result = parser.parse();
        // семантический анализ
        SemanticAnalyzer analyzer = new SemanticAnalyzer();
        analyzer.analyze(result);
        // компилируем
        if (!imported.contains(pathString)) {
            WattCompiler.compile(
                fileName,
                result,
                true
            );
            imported.add(pathString);
        }
    }
}
