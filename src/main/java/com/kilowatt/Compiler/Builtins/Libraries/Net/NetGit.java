package com.kilowatt.Compiler.Builtins.Libraries.Net;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattList;
import com.kilowatt.Compiler.Builtins.Libraries.Std.Fs.FsPath;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.IOException;

/*
Net -> Git
 */
public class NetGit {
    public Git clone(String url, String branch, FsPath path) {
        try {
            return Git.cloneRepository()
                .setURI(url)
                .setBranch(branch)
                .setDirectory(path.getPath().toFile())
                .call();
        } catch (GitAPIException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                address,
                "git error: " + e.getMessage(),
                "error command: clone. check your code."
            );
        }
    }

    public Git init(FsPath path) {
        try {
            return Git.init()
                .setDirectory(path.getPath().toFile())
                .call();
        } catch (GitAPIException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address,
                    "git error: " + e.getMessage(),
                    "error command: call. check your code."
            );
        }
    }

    public Git open(FsPath path) {
        try {
            return Git.open(path.getPath().toFile());
        } catch (IOException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address,
                    "git io error: " + e.getMessage(),
                    "error command: open. check your code."
            );
        }
    }

    public void branch(Git git, String branch, boolean force) {
        try {
            git.branchCreate()
                .setName(branch)
                .setForce(force)
                .call();
        } catch (GitAPIException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                address,
                "git error: " + e.getMessage(),
                "error command: branch new. check your code."
            );
        }
    }

    public void branch_delete(Git git, String branch, boolean force) {
        try {
            git.branchDelete()
                .setBranchNames(branch)
                .setForce(force)
                .call();
        } catch (GitAPIException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                address,
                "git error: " + e.getMessage(),
                "error command: branch delete. check your code."
            );
        }
    }

    public void branch_rename(Git git, String branch, String newName) {
        try {
            git.branchRename()
                .setOldName(branch)
                .setNewName(newName)
                .call();
        } catch (GitAPIException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address,
                    "git error: " + e.getMessage(),
                    "error command: branch rename. check your code."
            );
        }
    }

    public WattList branch_list(Git git) {
        try {
            WattList list = new WattList();
            for (var branch : git.branchList().call()) {
                list.add(branch.getName());
            }
            return list;
        } catch (GitAPIException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address,
                    "git error: " + e.getMessage(),
                    "error command: branch list. check your code."
            );
        }
    }

    public void commit(Git git, String name, boolean verify, String authorName, String authorMail) {
        try {
            git.commit()
                .setMessage(name)
                .setNoVerify(!verify)
                .setAuthor(authorName, authorMail)
                .call();
        } catch (GitAPIException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address,
                    "git error: " + e.getMessage(),
                    "error command: commit. check your code."
            );
        }
    }

    public void push(Git git, String origin, boolean force, String username, String password) {
        try {
            git.push()
                .setForce(force)
                .setRemote(origin)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(
                    username, password
                ))
                .call();
        } catch (GitAPIException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                address,
                "git error: " + e.getMessage(),
                "error command: push by password. check your code."
            );
        }
    }

    public void pull(Git git, String origin) {
        try {
            git.pull()
                .setRemote(origin)
                .call();
        } catch (GitAPIException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                address,
                "git error: " + e.getMessage(),
                "error command: pull. check your code."
            );
        }
    }

    public void add(Git git, String file) {
        try {
            git.add()
                .addFilepattern(file)
                .call();
        } catch (GitAPIException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                address,
                "git error: " + e.getMessage(),
                "error command: add. check your code."
            );
        }
    }

    public void checkout(Git git, String commit, FsPath path, boolean force) {
        try {
            git.checkout()
                .addPath(path.toString())
                .setName(commit)
                .setForced(force)
                .call();
        } catch (GitAPIException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address,
                    "git error: " + e.getMessage(),
                    "error command: add. check your code."
            );
        }
    }
}