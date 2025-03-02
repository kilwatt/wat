package com.kea.Parser.AST;

import com.kea.Compiler.Builtins.Libraries.Collections.KeaList;
import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.Instructions.VmInstructionCall;
import com.kea.KeaVM.Instructions.VmInstructionDuplicate;
import com.kea.KeaVM.Instructions.VmInstructionPush;
import com.kea.KeaVM.VmAddress;
import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

/*
Список
 */
@Getter
@AllArgsConstructor
public class ListNode implements Node {
    private final Token location;
    private final ArrayList<Node> list;

    @Override
    public void compile() {
        VmAddress address = location.asAddress();
        KeaCompiler.code.visitInstruction(new VmInstructionPush(address, new KeaList()));
        for (Node node : list) {
            KeaCompiler.code.visitInstruction(new VmInstructionDuplicate(address));
            VmBaseInstructionsBox container = new VmBaseInstructionsBox();
            KeaCompiler.code.writeTo(container);
            node.compile();
            KeaCompiler.code.endWrite();
            KeaCompiler.code.visitInstruction(new VmInstructionCall(address, "add", container, true, false));
        }
    }
}
