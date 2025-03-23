package com.kilowatt.Parser.AST;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattList;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Semantic.SemanticAnalyzer;
import com.kilowatt.WattVM.Boxes.VmBaseInstructionsBox;
import com.kilowatt.WattVM.Instructions.VmInstructionCall;
import com.kilowatt.WattVM.Instructions.VmInstructionDuplicate;
import com.kilowatt.WattVM.VmAddress;
import com.kilowatt.Lexer.Token;
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
        WattCompiler.code.visitInstruction((vm, _) -> vm.push(new WattList()));
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
