package com.kilowatt.Compiler.Builtins.Libraries.Utils;

import java.util.UUID;

/*
Utils -> Uuid
 */
public class UtilsUuid {
    public String uuid() {
        return UUID.randomUUID().toString();
    }
}
