package com.kilowatt.Compiler.Builtins.Libraries.Std.Fs;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattList;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/*
Путь в файловой системе
 */
@AllArgsConstructor
@Getter
public class FsPath {
    private final Path path;

    public String filename() {
        return path.getFileName().toString();
    }

    public FsPath resolve(String second) {
        return new FsPath(path.resolve(second));
    }

    public FsPath resolve_path(Path second) {
        return new FsPath(path.resolve(second));
    }

    public FsPath root() {
        Path root = path.getRoot();
        return root == null ? null : new FsPath(root);
    }

    public FsPath parent() {
        Path parent = path.getParent();
        return parent == null ? null : new FsPath(parent);
    }

    public FsPath subpath(int from, int to) {
        return new FsPath(path.subpath(from, to));
    }

    public FsPath to_absolute() {
        return new FsPath(path.toAbsolutePath());
    }

    public boolean ends_with(FsPath second) {
        return path.endsWith(second.path);
    }

    public boolean starts_with(FsPath second) {
        return path.startsWith(second.path);
    }

    public WattList files() {
        try (Stream<Path> stream = Files.list(path)) {
            return WattList.of(stream.map(FsPath::new).toArray());
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address,
                    "io error in fs: " + e.getMessage(),
                    "check file exists."
            );
        }
    }

    public boolean is_directory() {
        return Files.isDirectory(path);
    }

    @Override
    public String toString() {
        return path.toString();
    }

    public String to_string() {
        return this.toString();
    }
}
