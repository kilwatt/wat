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
    public void resolve(VmAddress addr, String name, String fullNameOverride) {
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
                            addr.getLine(),
                            addr.getFileName(),
                            "couldn't resolve name: " + name,
                            "check file exists!");
                    }
                } catch (NullPointerException e) {
                    throw new WattResolveError(
                        addr.getLine(),
                        addr.getFileName(),
                        "couldn't resolve name: " + name,
                        "check file exists!");
                }
            }
        } catch (IOException e) {
            throw new WattResolveError(
                addr.getLine(),
                addr.getFileName(),
                "couldn't resolve name: " + localPath.resolve(name),
                "check file exists!");
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
            WattCompiler.importDefinitions(
                addr,
                result
            );
            imported.add(pathString);
        }
    }
}
