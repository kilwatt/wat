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
                address,
                "io error in fs (" + path + "): " + e.getMessage(),
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
                    address,
                    "io error in fs (" + path + "): " + e.getMessage(),
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
                    address,
                    "io error in fs (" + path + "): " + e.getMessage(),
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
                    address,
                    "io error in fs (" + path + "): " + e.getMessage(),
                    "check file exists."
            );
        }
    }

    public boolean is_exists(Path path) {
        return Files.exists(path);
    }

    public void create_file(Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address,
                    "io error in fs (" + path + "): " + e.getMessage(),
                    "check file exists."
            );
        }
    }

    public void create_directory(Path path) {
        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address,
                    "io error in fs (" + path + "): " + e.getMessage(),
                    "check file exists."
            );
        }
    }

    public void delete_path(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address,
                    "io error in fs (" + path + "): " + e.getMessage(),
                    "check file exists."
            );
        }
    }
}
