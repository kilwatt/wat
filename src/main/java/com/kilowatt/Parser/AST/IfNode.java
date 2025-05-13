package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.Instructions.VmInstructionIf;
import com.kilowatt.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/*
If
 */
@AllArgsConstructor
@Getter
public class IfNode implements Node {
    private final Token location;
    private final Node node;
    private final Node logical;
    @Setter
    private IfNode elseNode;

    @Override
    public void compile() {
        // финальная компиляция
        WattCompiler.code.visitInstruction(getCompiled());
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        logical.analyze(analyzer);
        node.analyze(analyzer);
        if (elseNode != null) elseNode.analyze(analyzer);
    }

    public VmInstructionIf getCompiled() {
        // условия
        VmChunk conditions = new VmChunk();
        WattCompiler.code.writeTo(conditions);
        logical.compile();
        WattCompiler.code.endWrite();
        // тело
        VmInstructionIf vmInstructionIf = new VmInstructionIf(location.asAddress());
        vmInstructionIf.setConditions(conditions);
        WattCompiler.code.writeTo(vmInstructionIf.getBody());
        node.compile();
        WattCompiler.code.endWrite();
        // else
        if (elseNode != null) {
            vmInstructionIf.setElseInstruction(elseNode.getCompiled());
        }
        // возвращаем
        return vmInstructionIf;
    }
}
