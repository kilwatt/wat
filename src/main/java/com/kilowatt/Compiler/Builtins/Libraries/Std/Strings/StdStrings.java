package com.kilowatt.Compiler.Builtins.Libraries.Std.Strings;

import org.apache.commons.text.StringEscapeUtils;

/*
Стд -> Строки
 */
public class StdStrings {
    // распарсинг escape-последовательности
    public String unescape(String escape) {
        return StringEscapeUtils.unescapeJava(escape);
    }
}
