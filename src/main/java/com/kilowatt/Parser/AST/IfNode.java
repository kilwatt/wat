package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Instructions.VmInstructionIf;
import com.kilowatt.WattVM.VmAddress;
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
        VmBaseInstructionsBox conds = new VmBaseInstructionsBox();
        WattCompiler.code.writeTo(conds);
        logical.compile();
        WattCompiler.code.endWrite();
        VmInstructionIf vmInstructionIf
                = new VmInstructionIf(location.asAddress());
        vmInstructionIf.setConditions(conds);
        WattCompiler.code.writeTo(vmInstructionIf.getInstructions());
        node.compile();
        WattCompiler.code.endWrite();
        WattCompiler.code.visitInstruction(vmInstructionIf);
        if (elseNode != null) {
            vmInstructionIf.setElseInstruction(elseNode.getCompiled());
        }
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        logical.analyze(analyzer);
        node.analyze(analyzer);
    }

    public VmInstructionIf getCompiled() {
        VmBaseInstructionsBox conds = new VmBaseInstructionsBox();
        WattCompiler.code.writeTo(conds);
        logical.compile();
        WattCompiler.code.endWrite();
        VmInstructionIf vmInstructionIf
                = new VmInstructionIf(new VmAddress(location.getFileName(), location.getLine()));
        vmInstructionIf.setConditions(conds);
        WattCompiler.code.writeTo(vmInstructionIf.getInstructions());
        node.compile();
        WattCompiler.code.endWrite();
        return vmInstructionIf;
    }
}
