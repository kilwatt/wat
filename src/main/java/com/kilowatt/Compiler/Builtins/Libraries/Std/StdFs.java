package com.kilowatt.Compiler.Builtins.Libraries.Std;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/*
Стд -> FS (файловая система)
 */
public class StdFs {
    public FileReader open_reader(String path) {
        try {
            return new FileReader(path);
        } catch (FileNotFoundException e) {
            VmAddress address = WattCompiler.vm.getReflection().getLastCallInfo().getAddress();
            throw new WattRuntimeError(
                address.getLine(),
                address.getFileName(),
                "file not found: " + path,
                "check file exists."
            );
        }
    }

    public FileWriter open_writer(String path) {
        try {
            return new FileWriter(path, false);
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getReflection().getLastCallInfo().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "io exception in fs: " + e.getMessage(),
                    "check file exists."
            );
        }
    }

    public boolean is_exists(String path) {
        return Files.exists(Path.of(path));
    }

    public void create_file(String path) {
        try {
            Files.createFile(Path.of(path));
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getReflection().getLastCallInfo().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "io exception in fs: " + e.getMessage(),
                    "check file exists."
            );
        }
    }

    public void create_directory(String path) {
        try {
            Files.createDirectory(Path.of(path));
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getReflection().getLastCallInfo().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "io exception in fs: " + e.getMessage(),
                    "check file exists."
            );
        }
    }

    public void delete_path(String path) {
        try {
            Files.delete(Path.of(path));
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getReflection().getLastCallInfo().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "io exception in fs: " + e.getMessage(),
                    "check file exists."
            );
        }
    }
}
