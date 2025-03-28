package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattList;
import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattMap;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Instructions.VmInstruction;
import com.kilowatt.WattVM.Instructions.VmInstructionCall;
import com.kilowatt.WattVM.Instructions.VmInstructionDuplicate;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.Lexer.Token;
import com.kilowatt.WattVM.VmFrame;
import com.kilowatt.WattVM.WattVM;
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
        WattCompiler.code.visitInstruction(new VmInstruction() {
            @Override
            public void run(WattVM vm, VmFrame<String, Object> scope) {
                vm.push(new WattMap());
            }

            @Override
            public String toString() {
                return "MAP()";
            }
        });
        for (Node node : map.keySet()) {
            WattCompiler.code.visitInstruction(new VmInstructionDuplicate(address));
            VmBaseInstructionsBox container = new VmBaseInstructionsBox();
            WattCompiler.code.writeTo(container);
            node.compile();
            map.get(node).compile();
            WattCompiler.code.endWrite();
            WattCompiler.code.visitInstruction(new VmInstructionCall(address, "set", container, true, false));
        }
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        for (Node key : map.keySet()) {
            key.analyze(analyzer);
            map.get(key).analyze(analyzer);
        }
    }
}
