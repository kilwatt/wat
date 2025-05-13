package com.kilowatt.Compiler.Builtins.Libraries.Net;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.*;
import com.kilowatt.WattVM.Entities.VmFunction;
import com.kilowatt.WattVM.Threads.VmThreadPool;
import com.kilowatt.WattVM.VmAddress;
import io.javalin.Javalin;
import lombok.SneakyThrows;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/*
Net -> HttpServer
 */
public class NetHttpServer {
    public Javalin app() {
        return Javalin.create(javalinConfig -> {
            // настраиваем работу с потоками под Ватт
            QueuedThreadPool threadPool = new VmThreadPool(WattCompiler.vm);
            threadPool.setName("JavalinWatt");
            threadPool.setMinThreads(2);
            javalinConfig.jetty.threadPool = threadPool;
        });
    }

    @SneakyThrows
    public void run(Javalin app, int port) {
        app.start(port);
    }

    private void execFnSafely(VmFunction fn) {
        try {
            fn.exec(WattCompiler.vm, false);
        } catch (WattError error) {
            error.panic();
        } catch (RuntimeException error) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            new WattRuntimeError(
                address,
                "jvm runtime exception: " + error.getMessage(),
                "check your code."
            ).panic();
        }
    }

    private void checkFn(String path, VmFunction fn) {
        // получаем адрес
        VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
        // проверяем функцию
        if (fn.getParams().size() != 1) {
            throw new WattRuntimeError(
                address,
                "couldn't create handler " + path + ", fn should take 1 param.",
                "params: javalin context"
            );
        }
    }

    public void get(Javalin app, String path, VmFunction fn) {
        // проверка функции
        checkFn(path, fn);
        // установка хэндлера
        app.get(path, ctx -> {
            WattCompiler.vm.initForThread();
            WattCompiler.vm.push(ctx);
            execFnSafely(fn);
        });
    }

    public void post(Javalin app, String path, VmFunction fn) {
        // проверка функции
        checkFn(path, fn);
        // установка хэндлера
        app.post(path, ctx -> {
            WattCompiler.vm.initForThread();
            WattCompiler.vm.push(ctx);
            execFnSafely(fn);
        });
    }

    public void delete(Javalin app, String path, VmFunction fn) {
        // проверка функции
        checkFn(path, fn);
        // установка хэндлера
        app.delete(path, ctx -> {
            WattCompiler.vm.initForThread();
            WattCompiler.vm.push(ctx);
            execFnSafely(fn);
        });
    }

    public void patch(Javalin app, String path, VmFunction fn) {
        // проверка функции
        checkFn(path, fn);
        // установка хэндлера
        app.patch(path, ctx -> {
            WattCompiler.vm.initForThread();
            WattCompiler.vm.push(ctx);
            execFnSafely(fn);
        });
    }

    public void put(Javalin app, String path, VmFunction fn) {
        // проверка функции
        checkFn(path, fn);
        // установка хэндлера
        app.put(path, ctx -> {
            WattCompiler.vm.initForThread();
            WattCompiler.vm.push(ctx);
            execFnSafely(fn);
        });
    }

    public void head(Javalin app, String path, VmFunction fn) {
        // проверка функции
        checkFn(path, fn);
        // установка хэндлера
        app.head(path, ctx -> {
            WattCompiler.vm.initForThread();
            WattCompiler.vm.push(ctx);
            execFnSafely(fn);
        });
    }

    public void options(Javalin app, String path, VmFunction fn) {
        // проверка функции
        checkFn(path, fn);
        // установка хэндлера
        app.options(path, ctx -> {
            WattCompiler.vm.initForThread();
            WattCompiler.vm.push(ctx);
            execFnSafely(fn);
        });
    }
}
