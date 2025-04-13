package com.kilowatt.Compiler.Builtins.Libraries.Std;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/*
Стд -> FS (файловая система)
 */
public class StdFs {
    public Path path_of(String path) {
        return Path.of(path);
    }

    public String read_text(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                address.getLine(),
                address.getFileName(),
                "io exception in fs: " + path,
                "check file exists."
            );
        }
    }

    public void write_text(Path path, String value) {
        try {
            Files.writeString(path, value);
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "io exception in fs: " + e.getMessage(),
                    "check file exists."
            );
        }
    }

    public byte[] read_bytes(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "io exception in fs: " + path,
                    "check file exists."
            );
        }
    }

    public void write_bytes(Path path, byte[] bytes) {
        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "io exception in fs: " + e.getMessage(),
                    "check file exists."
            );
        }
    }

    public boolean is_exists(Path path) {
        return Files.exists(path);
    }

    public void create_file(String path) {
        try {
            Files.createFile(Path.of(path));
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
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
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
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
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "io exception in fs: " + e.getMessage(),
                    "check file exists."
            );
        }
    }
}
