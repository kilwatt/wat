package com.kea;

import com.kea.Errors.KeaColors;
import com.kea.Executor.KeaExecutor;

import java.io.IOException;

public class Kea {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            warning(KeaColors.ANSI_YELLOW + "Invalid usage. Example: kea <script>");
        }
        KeaExecutor.run(args[0]);
    }

    public static void warning(String warning) {
        System.out.println(warning);
        System.exit(-1);
    }
}