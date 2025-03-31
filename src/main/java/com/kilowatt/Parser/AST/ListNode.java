package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattList;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Instructions.VmInstruction;
import com.kilowatt.WattVM.Instructions.VmInstructionCall;
import com.kilowatt.WattVM.Instructions.VmInstructionDuplicate;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.Lexer.Token;
import com.kilowatt.WattVM.VmCodeDumper;
import com.kilowatt.WattVM.VmFrame;
import com.kilowatt.WattVM.WattVM;
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
        WattCompiler.code.visitInstruction(new VmInstruction() {
            @Override
            public void run(WattVM vm, VmFrame<String, Object> scope) {
                vm.push(new WattList());
            }

            @Override
            public void print(int indent) {
                VmCodeDumper.dumpLine(indent, "LIST()");
            }

            @Override
            public String toString() {
                return "LIST()";
            }
        });
        for (Node node : list) {
            WattCompiler.code.visitInstruction(new VmInstructionDuplicate(address));
            VmBaseInstructionsBox container = new VmBaseInstructionsBox();
            WattCompiler.code.writeTo(container);
            node.compile();
            WattCompiler.code.endWrite();
            WattCompiler.code.visitInstruction(new VmInstructionCall(address, "add", container, true, false));
        }
    }

    @Override
    public void analyze(SemanticAnalyzer analyzer) {
        for (Node value : list) {
            value.analyze(analyzer);
        }
    }
}
