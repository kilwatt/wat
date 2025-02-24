package com.kea.Parser.AST;

import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.Instructions.VmInstructionIf;
import com.kea.KeaVM.VmAddress;
import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Setter;

/*
If
 */
@AllArgsConstructor
public class IfNode implements Node {
    private final Token location;
    private final BlockNode node;
    private final Node logical;
    @Setter
    private IfNode elseNode;

    @Override
    public void compile() {
        VmBaseInstructionsBox conds = new VmBaseInstructionsBox();
        KeaCompiler.code.writeTo(conds);
        logical.compile();
        KeaCompiler.code.endWrite();
        VmInstructionIf vmInstructionIf
                = new VmInstructionIf(location.asAddress());
        vmInstructionIf.setConditions(conds);
        KeaCompiler.code.writeTo(vmInstructionIf.getInstructions());
        node.compile();
        KeaCompiler.code.endWrite();
        KeaCompiler.code.visitInstruction(vmInstructionIf);
        if (elseNode != null) {
            vmInstructionIf.setElseInstruction(elseNode.getCompiled());
        }
    }

    public VmInstructionIf getCompiled() {
        VmBaseInstructionsBox conds = new VmBaseInstructionsBox();
        KeaCompiler.code.writeTo(conds);
        logical.compile();
        KeaCompiler.code.endWrite();
        VmInstructionIf vmInstructionIf
                = new VmInstructionIf(new VmAddress(location.getFileName(), location.getLine()));
        vmInstructionIf.setConditions(conds);
        KeaCompiler.code.writeTo(vmInstructionIf.getInstructions());
        node.compile();
        KeaCompiler.code.endWrite();
        return vmInstructionIf;
    }
}
