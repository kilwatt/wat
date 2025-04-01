package com.kilowatt.Executor;

import com.kilowatt.Compiler.Builtins.WattLibraries;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattResolveError;
import com.kilowatt.Lexer.Lexer;
import com.kilowatt.Parser.AST.BlockNode;
import com.kilowatt.Parser.AST.ImportNode;
import com.kilowatt.Parser.Parser;
import com.kilowatt.Semantic.SemanticAnalyzer;
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
    public void resolve(ImportNode.WattImport wattImport) {
        // имя файла
        String name = wattImport.getName().value;
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
                fileName = WattLibraries.libraries.get(name);
                pathString = "/" + fileName;
                try (InputStream stream = WattExecutor.class.getResourceAsStream(pathString)) {
                    if (stream != null) {
                        lexer = new Lexer(fileName, new String(stream.readAllBytes()));
                    } else {
                        throw new WattResolveError(
                            wattImport.getName().getLine(),
                            wattImport.getName().getFileName(),
                            "couldn't resolve name: " + name,
                            "check file exists!");
                    }
                } catch (NullPointerException e) {
                    throw new WattResolveError(
                        wattImport.getName().getLine(),
                        wattImport.getName().getFileName(),
                        "couldn't resolve name: " + name,
                        "check file exists!");
                }
            }
        } catch (IOException e) {
            throw new WattResolveError(
                wattImport.getName().getLine(),
                wattImport.getName().getFileName(),
                "couldn't resolve name: " + localPath.resolve(name),
                "check file exists!");
        }
        // парсинг
        Parser parser = new Parser(fileName, lexer.scan());
        BlockNode result = parser.parse();
        // семантический анализ
        SemanticAnalyzer analyzer = new SemanticAnalyzer();
        analyzer.analyze(result);
        // компилируем
        if (!imported.contains(pathString)) {
            WattCompiler.importDefinitions(result, wattImport.isShouldImportAsModule());
            imported.add(pathString);
        }
    }
}
