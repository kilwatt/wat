package com.kilowatt.Compiler.Builtins.Libraries.Std.Fs;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattList;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/*
Стд -> FS (файловая система)
 */
public class StdFs {
    public FsPath path_of(String path) {
        return new FsPath(Path.of(path));
    }

    public String read_text(FsPath path) {
        try {
            return Files.readString(path.getPath());
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                address,
                "io error in fs: " + e.getMessage(),
                "check file exists."
            );
        }
    }

    public void write_text(FsPath path, String value) {
        try {
            Files.writeString(path.getPath(), value);
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address,
                    "io error in fs: " + e.getMessage(),
                    "check file exists."
            );
        }
    }

    public byte[] read_bytes(FsPath path) {
        try {
            return Files.readAllBytes(path.getPath());
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address,
                    "io error in fs: " + e.getMessage(),
                    "check file exists."
            );
        }
    }

    public void write_bytes(FsPath path, byte[] bytes) {
        try {
            Files.write(path.getPath(), bytes);
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address,
                    "io error in fs: " + e.getMessage(),
                    "check file exists."
            );
        }
    }

    public boolean is_exists(FsPath path) {
        return Files.exists(path.getPath());
    }

    public void create_file(FsPath path) {
        try {
            Files.createFile(path.getPath());
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address,
                    "io error in fs: " + e.getMessage(),
                    "check file exists."
            );
        }
    }

    public void create_directory(FsPath path) {
        try {
            Files.createDirectory(path.getPath());
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address,
                    "io error in fs: " + e.getMessage(),
                    "check file exists."
            );
        }
    }

    public void delete_path(FsPath path) {
        try {
            Files.delete(path.getPath());
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address,
                    "io error in fs: " + e.getMessage(),
                    "check file exists."
            );
        }
    }

    public void copy_path(FsPath from, FsPath to) {
        try {
            Files.copy(from.getPath(), to.getPath());
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address,
                    "io error in fs: " + e.getMessage(),
                    "check file exists."
            );
        }
    }

    public void move_path(FsPath from, FsPath to) {
        try {
            Files.move(from.getPath(), to.getPath());
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address,
                    "io error in fs: " + e.getMessage(),
                    "check file exists."
            );
        }
    }
}
