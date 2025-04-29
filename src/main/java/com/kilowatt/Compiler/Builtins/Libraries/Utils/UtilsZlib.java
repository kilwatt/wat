package com.kilowatt.Compiler.Builtins.Libraries.Utils;

/*
Utils -> Zlib
 */
public class UtilsZlib {
    public byte[] new_bytes_array(int len) {
        return new byte[len];
    }
    public int len(byte[] bytes) {
        return bytes.length;
    }
}
