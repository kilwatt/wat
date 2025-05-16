package com.kilowatt.Compiler;

import com.kilowatt.Errors.WattColors;
import com.kilowatt.WattVM.Benchmark.VmBenchmark;
import com.kilowatt.WattVM.Codegen.WattVmCode;
import com.kilowatt.WattVM.WattVM;
import com.kilowatt.Parser.AST.*;
import lombok.Getter;

/*
Компилятор
 */
@Getter
public class WattCompiler {
    // код вм
    public static WattVmCode code = new WattVmCode();
    // вм
    public static WattVM vm = new WattVM();

    // ресет компилятора и вм
    public static void reset() {
        code = new WattVmCode();
        vm = new WattVM();
    }

    // компиляция
    public static void compile(String filename, Node node, boolean isImport) {
        // компилируем
        // если не импорт
        if (!isImport) {
            node.compile();
        }
        // если импорт
        else {
            if (node instanceof BlockNode block) {
                for (Node inNode : block.getNodes()) {
                    if (inNode instanceof UnitNode ||
                            inNode instanceof TypeNode ||
                            inNode instanceof FnNode ||
                            inNode instanceof NativeNode ||
                            inNode instanceof ImportNode ||
                            inNode instanceof TraitNode) {
                        inNode.compile();
                    }
                }
            } else {
                throw new RuntimeException(
                    "not a block, while importing: " + node
                );
            }
        }
    }
}
