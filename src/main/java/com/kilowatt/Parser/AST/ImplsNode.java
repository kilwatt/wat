package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Lexer.Token;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Chunks.VmChunk;
import com.kilowatt.WattVM.Instructions.VmInstruction;
import com.kilowatt.WattVM.Instructions.VmInstructionImpls;
import com.kilowatt.WattVM.Instructions.VmInstructionPush;
import com.kilowatt.WattVM.Storage.VmFrame;
import com.kilowatt.WattVM.WattVM;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Имплементирует ли
 */
@AllArgsConstructor
@Getter
public class ImplsNode implements Node {
    private final Node variable;
    private final Token traitName;

    @Override
    public void compile() {
        variable.compile();
        WattCompiler.code.visitInstruction(new VmInstructionImpls(
            traitName.asAddress(),
            traitName.value
        ));
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        analyzer.analyze(variable);
    }
}
