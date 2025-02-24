package com.kea;

import com.kea.Errors.KeaParsingError;
import com.kea.Lexer.Lexer;
import com.kea.Parser.AST.BlockNode;
import com.kea.Parser.AST.Node;
import com.kea.Parser.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Kea {
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer("test.kea", new String(Files.readAllBytes(Path.of(args[0]))));
        Node result = null;
        try {
            Parser parser = new Parser("test.kea", lexer.scan());
            result = parser.parse();
        } catch (KeaParsingError e) {
            e.print();
            // e.printStackTrace();
        }
        System.out.println();
    }
}