package com.kilowatt.Compiler.Builtins.Libraries.Std.Fs;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.file.Path;

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

    public FsPath resolve(Path resolve) {
        return new FsPath(path.resolve(resolve));
    }

    public FsPath root() {
        Path root = path.getRoot();
        return root == null ? null : new FsPath(root);
    }

    public FsPath parent() {
        Path parent = path.getRoot();
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

    @Override
    public String toString() {
        return path.toString();
    }

    public String to_string() {
        return this.toString();
    }
}
