package com.kea.Parser.AST;

import com.kea.Compiler.Builtins.Libraries.Collections.KeaMap;
import com.kea.Compiler.KeaCompiler;
import com.kea.KeaVM.Boxes.VmBaseInstructionsBox;
import com.kea.KeaVM.Instructions.VmInstructionCall;
import com.kea.KeaVM.Instructions.VmInstructionDuplicate;
import com.kea.KeaVM.Instructions.VmInstructionPush;
import com.kea.KeaVM.VmAddress;
import com.kea.Lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;

/*
Словарь
 */
@Getter
@AllArgsConstructor
public class MapNode implements Node {
    private final Token location;
    private final HashMap<Node, Node> map;

    @Override
    public void compile() {
        VmAddress address = location.asAddress();
        KeaCompiler.code.visitInstruction(new VmInstructionPush(address, new KeaMap()));
        for (Node node : map.keySet()) {
            KeaCompiler.code.visitInstruction(new VmInstructionDuplicate(address));
            VmBaseInstructionsBox container = new VmBaseInstructionsBox();
            KeaCompiler.code.writeTo(container);
            node.compile();
            map.get(node).compile();
            KeaCompiler.code.endWrite();
            KeaCompiler.code.visitInstruction(new VmInstructionCall(address, "set", container, true, false));
        }
    }
}
